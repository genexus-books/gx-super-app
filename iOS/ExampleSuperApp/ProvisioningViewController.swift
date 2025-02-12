
//
//  ViewController.swift
//  ExampleSuperApp
//

import UIKit
import GXCoreUI
import GXSuperApp
#if SUPERAPPSANDBOX
import GXSuperAppSandbox
#endif // SUPERAPPSANDBOX

class ProvisioningViewController: UITableViewController {
	
	override func viewDidLoad() {
		super.viewDidLoad()
		tableView.register(MiniAppTableViewCell.self, forCellReuseIdentifier: Self.miniAppCellId)
		navigationItem.searchController = searchController
		searchController.searchBar.scopeButtonTitles = ["Text", "Tags"]
		searchController.searchResultsUpdater = self
		loadFromProvisioning()
		refreshControl?.addTarget(self, action: #selector(Self.pullToRefresh), for: .valueChanged)
#if SUPERAPPSANDBOX
		navigationItem.rightBarButtonItem = UIBarButtonItem(barButtonSystemItem: .camera,
															target: self,
															action: #selector(Self.loadSandbox))
#endif // SUPERAPPSANDBOX
	}
	
	// MARK: - Loading
	
	private var loadedMiniAppsInfo: LoadedInfo = .init(miniApps: [], searchType: SearchQueryInfo.empty.type)
	
	private var currentLoadOperation: GXCancelableOperation? = nil {
		didSet {
			guard oldValue !== currentLoadOperation else { return }
			oldValue?.cancel()
		}
	}
	
	private var curretSearchInfo: SearchQueryInfo {
		if searchController.isActive {
			let type: SearchType = searchController.searchBar.selectedScopeButtonIndex == 1 ? .tag : .text
			let text = searchController.searchBar.text ?? ""
			return .init(text: text, type: type)
		}
		return .empty
	}
	private var lastLoadedSearchInfo = SearchQueryInfo.empty
	
	private func loadFromProvisioning(reload: ReloadType? = nil) {
		let searchInfo = curretSearchInfo
		lastLoadedSearchInfo = searchInfo
		if reload == .cleanFirst {
			loadedMiniAppsInfo = .init(miniApps: [], searchType: searchInfo.type)
			tableView?.reloadData()
		}
		let pageSize = Self.loadPageSize
		let start: Int = {
			guard reload == nil, loadedMiniAppsInfo.searchType == searchInfo.type else { return 0 }
			return loadedMiniAppsInfo.miniApps.count
		}()
		let completion: GXSuperApp.GXSuperAppProvisioning.MiniAppsInfoCompletion = { [weak self] result in
			DispatchQueue.main.async {
				guard let sself = self, sself.currentLoadOperation != nil, sself.lastLoadedSearchInfo == searchInfo else { return }
				defer { sself.refreshControl?.endRefreshing() }
				sself.currentLoadOperation = nil
				switch result {
				case .failure(let error):
					/// Handle error
					print(error.localizedDescription)
				case .success(let miniAppsInfo):
					if sself.loadedMiniAppsInfo.searchType != searchInfo.type || reload == .replaceAfter {
						sself.loadedMiniAppsInfo = .init(miniApps: miniAppsInfo,
														 searchType: searchInfo.type,
														 allPagesLoaded: miniAppsInfo.count < pageSize)
						sself.tableView?.reloadData()
					}
					else {
						let oldAllPagesLoaded = sself.loadedMiniAppsInfo.allPagesLoaded
						let newMiniAppsInfo = sself.loadedMiniAppsInfo.miniApps + miniAppsInfo
						sself.loadedMiniAppsInfo = .init(miniApps: newMiniAppsInfo,
														 searchType: searchInfo.type,
														 allPagesLoaded: oldAllPagesLoaded || miniAppsInfo.count < pageSize)
						let firstNewIndex = sself.loadedMiniAppsInfo.miniApps.count - miniAppsInfo.count
						if firstNewIndex == 0 {
							sself.tableView?.reloadData()
						}
						else {
							let insertedIndexPaths: [IndexPath] = miniAppsInfo.enumerated().map {
								.init(row: firstNewIndex + $0.offset, section: 0)
							}
							sself.tableView?.insertRows(at: insertedIndexPaths, with: .none)
						}
					}
				}
			}
		}
		currentLoadOperation = {
			switch searchInfo.type {
			case .text:
				return GXSuperAppProvisioning.miniAppsInfoByText(searchInfo.text, start: start, count: pageSize, completion: completion)
			case .tag:
				return GXSuperAppProvisioning.miniAppsInfoByTag(searchInfo.text, start: start, count: pageSize, completion: completion)
			}
		}()
	}
	
	// MARK: Table View
	
	private let searchController = UISearchController(searchResultsController: nil)
	private static let miniAppCellId = "miniAppCell"
	
	@objc private func pullToRefresh() {
		loadFromProvisioning(reload: .replaceAfter)
	}
	
	override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
		return section == 0 ? loadedMiniAppsInfo.miniApps.count : 0
	}
	
	override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
		let cell = tableView.dequeueReusableCell(withIdentifier: Self.miniAppCellId, for: indexPath) as! MiniAppTableViewCell
		let miniApp = loadedMiniAppsInfo.miniApps[indexPath.row]
		cell.miniAppInfo = miniApp
		updateCellLoadingIndicator(cell)
		return cell
	}
	
	private var loadingMiniApp: GXMiniAppInformation? = nil
	
	override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
		tableView.deselectRow(at: indexPath, animated: true)
		guard let cell = tableView.cellForRow(at: indexPath) as! MiniAppTableViewCell?, let miniApp = cell.miniAppInfo else {
			return
		}
		loadMiniApp(miniApp, from: cell)
	}
	
	override func tableView(_ tableView: UITableView, willDisplay cell: UITableViewCell, forRowAt indexPath: IndexPath) {
		guard !loadedMiniAppsInfo.allPagesLoaded, indexPath.row + 1 == loadedMiniAppsInfo.miniApps.count, currentLoadOperation == nil else { return }
		loadFromProvisioning()
	}
	
	private func updateCellLoadingIndicator(_ cell: MiniAppTableViewCell) {
		let indicator: UIActivityIndicatorView
		if cell.accessoryView is UIActivityIndicatorView {
			indicator = cell.accessoryView as! UIActivityIndicatorView
		}
		else {
			indicator = UIActivityIndicatorView(style: .medium)
			indicator.hidesWhenStopped = true
			cell.accessoryView = indicator
		}
		let isLoadingMiniApp = loadingMiniApp?.id == cell.miniAppInfo.id
		if (indicator.isAnimating != isLoadingMiniApp) {
			if isLoadingMiniApp {
				indicator.startAnimating()
			}
			else {
				indicator.stopAnimating()
			}
		}
	}
	
	private func visibleCell(for miniAppId: String) -> MiniAppTableViewCell? {
		tableView?.visibleCells.compactMapFirst {
			guard let miniAppCell = $0 as? MiniAppTableViewCell, miniAppCell.miniAppInfo.id == miniAppId else {
				return nil
			}
			return miniAppCell
		}
	}
	
	private func loadMiniApp(_ miniApp: GXMiniAppInformation, from miniAppCell: MiniAppTableViewCell? = nil) {
		guard loadingMiniApp == nil else {
			if loadingMiniApp?.id != miniApp.id {
				print("Mini app \(loadingMiniApp!.id) is still loading...")
			}
			return
		}
		loadingMiniApp = miniApp
		if let cell = miniAppCell ?? visibleCell(for: miniApp.id) {
			updateCellLoadingIndicator(cell)
		}
		
		/// Call GXMiniProgramLoader loadMiniProgram method to load a mini app. Use completion handler to handle error or to provide feedback.
		/// Loading the mini app will hide UIApplication.shared.keyWindow, and it will be restored when the mini app exits to the host super app.
		
		GXMiniAppsManager.loadMiniApp(info: miniApp) { [weak self] error in
			DispatchQueue.main.async {
				guard let sself = self, sself.loadingMiniApp?.id == miniApp.id else { return }
				sself.loadingMiniApp = nil
				if let cell = sself.visibleCell(for: miniApp.id) {
					sself.updateCellLoadingIndicator(cell)
				}
				if let error {
#if SSO_ENABLED
					if case .authorizationSSO(let ssoError) = error {
						/// Handle SSO error.
						switch ssoError {
						case .scopeMissing(let scopes):
							/// Handling this error is required if GXSSOURLCheckMiniAppScope was configured (which returned is_allowed = false resulting in this error).
							sself.handleMissingScopes(scopes, miniApp: miniApp)
							return
						default:
							/// Handle other errors as needed.
							break
						}
					}
#endif // SSO_ENABLED
					print("There was an error loading mini app \(miniApp.id): \(error.localizedDescription)")
				}
			}
		}
	}
	
#if SUPERAPPSANDBOX
	// MARK: - Sandbox
	
	@objc private func loadSandbox() {
		let sandboxController = GXMiniAppsManager.sandboxLoaderViewController()
		/// Can also be pushed into a navigation controller, as in: navigationController?.pushViewController(sandboxController, animated: true)
		present(sandboxController, animated: true)
	}
#endif // SUPERAPPSANDBOX
	
#if SSO_ENABLED
	private func handleMissingScopes(_ scopes: String, miniApp: GXMiniAppInformation) {
		///	An option is to ask the user to accept the scopes
		let msg = "Mini App '\(miniApp.name)' needs your permission to access the following scopes: \(scopes)"
		let alertController = UIAlertController(title: nil, message: msg, preferredStyle: .alert)
		alertController.addAction(.init(title: "Accept", style: .default, handler: { [weak self] _ in
			/// Update where needed for GXSSOURLCheckMiniAppScope to return is_allowed = true on the next call, and then call GXMiniAppsManager.loadMiniApp(info: miniApp) again.
			guard let sself = self, sself.loadingMiniApp == nil else {
				return
			}
			sself.loadMiniApp(miniApp)
		}))
		alertController.addAction(.init(title: "Reject", style: .cancel))
		present(alertController, animated: true)
	}
#endif // SSO_ENABLED
}

private extension ProvisioningViewController {
	
	static let loadPageSize = 25
	
	struct LoadedInfo {
		var miniApps: [GXMiniAppInformation]
		var searchType: SearchType
		var allPagesLoaded: Bool = false
	}
	
	enum SearchType {
		case text
		case tag
	}
	
	struct SearchQueryInfo: Equatable {
		var text: String
		var type: SearchType
		
		static let empty: SearchQueryInfo = .init(text: "", type: .text)
	}
	
	private enum ReloadType {
		case cleanFirst
		case replaceAfter
	}
}

extension ProvisioningViewController: UISearchResultsUpdating {
	private static let searchUpdateDelay: TimeInterval = 0.300
	
	func updateSearchResults(for searchController: UISearchController) {
		guard searchController == self.searchController else { return }
		let delayedSearchInfo = curretSearchInfo
		guard lastLoadedSearchInfo != delayedSearchInfo else { return }
		guard delayedSearchInfo.type == lastLoadedSearchInfo.type else {
			loadFromProvisioning(reload: .cleanFirst)
			return
		}
		DispatchQueue.main.asyncAfter(deadline: .now() + Self.searchUpdateDelay) { [weak self] in
			guard let sself = self, delayedSearchInfo == sself.curretSearchInfo,
				  sself.lastLoadedSearchInfo != delayedSearchInfo else { return }
			sself.loadFromProvisioning(reload: .replaceAfter)
		}
	}
}

private class MiniAppTableViewCell: UITableViewCell {
	override init(style: UITableViewCell.CellStyle, reuseIdentifier: String?) {
		super.init(style: .default, reuseIdentifier: reuseIdentifier)
	}
	
	required init?(coder: NSCoder) {
		super.init(coder: coder)
	}
	
	var miniAppInfo: GXMiniAppInformation! {
		didSet {
			textLabel?.text = miniAppInfo.name
		}
	}
}
