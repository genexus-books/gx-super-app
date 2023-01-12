//
//  ViewController.swift
//  ExampleSuperApp
//

import UIKit
import GXCoreUI

class ProvisioningViewController: UITableViewController {
	
	override func viewDidLoad() {
		super.viewDidLoad()
		tableView.register(MiniAppTableViewCell.self, forCellReuseIdentifier: Self.miniAppCellId)
		navigationItem.searchController = searchController
		searchController.searchBar.scopeButtonTitles = ["Texto", "Tags"]
		searchController.searchResultsUpdater = self
		loadFromProvisioning()
		refreshControl?.addTarget(self, action: #selector(Self.pullToRefresh), for: .valueChanged)
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
		let completion: GXCoreBL.GXSuperAppProvisioning.MiniAppsInfoCompletion = { [weak self] result in
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
		cell.miniAppId = miniApp
		updateCellLoadingIndicator(cell)
		return cell
	}
	
	private var loadingMiniApp: GXMiniAppInformation? = nil
	
	override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
		tableView.deselectRow(at: indexPath, animated: true)
		guard let cell = tableView.cellForRow(at: indexPath) as! MiniAppTableViewCell?, let miniApp = cell.miniAppId else {
			return
		}
		guard loadingMiniApp == nil else {
			if loadingMiniApp?.id != miniApp.id {
				print("Mini app \(loadingMiniApp!.id) aun cargando...")
			}
			return
		}
		loadingMiniApp = miniApp
		updateCellLoadingIndicator(cell)
		
		/// Call GXMiniProgramLoader loadMiniProgram method to load a mini app. Use completion handler to handle error or to provide feedback.
		/// Loading the mini app will replace UIApplication.shared.keyWindow.rootViewController, and it will be restored when the mini app exits to the host super app.
		/// Note "GXMiniprogramsEnabled" boolean key is currently required in the applications Info.plist.
		
		GXMiniAppsManager.loadMiniApp(info: miniApp) { error in
			DispatchQueue.main.async {
				guard self.loadingMiniApp?.id == miniApp.id else { return }
				self.loadingMiniApp = nil
				if let cell = tableView.cellForRow(at: indexPath) as! MiniAppTableViewCell?, cell.miniAppId.id == miniApp.id {
					self.updateCellLoadingIndicator(cell)
				}
				if let error = error {
					print("Ha ocurrido un error al cargar la mini app \(miniApp.id): \(error.localizedDescription)")
				}
			}
		}
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
		let isLoadingMiniApp = loadingMiniApp?.id == cell.miniAppId.id
		if (indicator.isAnimating != isLoadingMiniApp) {
			if isLoadingMiniApp {
				indicator.startAnimating()
			}
			else {
				indicator.stopAnimating()
			}
		}
	}
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
	
	var miniAppId: GXMiniAppInformation! {
		didSet {
			textLabel?.text = miniAppId.name
		}
	}
}
