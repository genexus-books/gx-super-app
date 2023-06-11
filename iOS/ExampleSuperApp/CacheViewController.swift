//
//  CacheViewController.swift
//  ExampleSuperApp
//

import UIKit
import GXCoreUI
import GXSuperApp

class CacheViewController: UITableViewController {
	
	override func viewDidLoad() {
		super.viewDidLoad()
		tableView.register(CachedMiniAppTableViewCell.self, forCellReuseIdentifier: Self.cachedMiniAppCellId)
		refreshControl?.addTarget(self, action: #selector(Self.pullToRefresh), for: .valueChanged)
		navigationItem.rightBarButtonItem = UIBarButtonItem(barButtonSystemItem: .trash, target: self, action: #selector(Self.clearAll))
	}
	
	override func viewWillAppear(_ animated: Bool) {
		super.viewWillAppear(animated)
		reload()
	}
	
	@objc private func clearAll() {
		DispatchQueue.global(qos: .userInitiated).async {
			do {
				try GXMiniAppsManager.clearCachedMiniApps()
			}
			catch let error {
				/// Handle error
				print(error.localizedDescription)
			}
			DispatchQueue.main.async { [weak self] in
				self?.reload()
			}
		}
	}
	
	// MARK: - Loading
	
	private var loadedCachedMiniApps: [GXCachedMiniApp] = [] {
		didSet {
			navigationItem.rightBarButtonItem?.isEnabled = !loadedCachedMiniApps.isEmpty
		}
	}
		
	private func reload() {
		DispatchQueue.global(qos: .userInitiated).async {
			defer {
				DispatchQueue.main.async { [weak self] in
					self?.refreshControl?.endRefreshing()
				}
			}
			do {
				let cachedMiniApps = try GXMiniAppsManager.cachedMiniApps()
				DispatchQueue.main.async { [weak self] in
					guard let sself = self else { return }
					sself.loadedCachedMiniApps = cachedMiniApps.sorted(by: { $0.lastUsedDatetime > $1.lastUsedDatetime})
					sself.tableView?.reloadData()
				}
			}
			catch let error {
				/// Handle error
				print(error.localizedDescription)
			}
		}
	}
	
	// MARK: Table View
	
	private static let cachedMiniAppCellId = "cachedMiniAppCell"
	
	@objc private func pullToRefresh() {
		reload()
	}
	
	override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
		return section == 0 ? loadedCachedMiniApps.count : 0
	}
	
	override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
		let cell = tableView.dequeueReusableCell(withIdentifier: Self.cachedMiniAppCellId, for: indexPath) as! CachedMiniAppTableViewCell
		let cachedMiniApp = loadedCachedMiniApps[indexPath.row]
		cell.cachedMiniApp = cachedMiniApp
		return cell
	}
	
	override func tableView(_ tableView: UITableView, trailingSwipeActionsConfigurationForRowAt indexPath: IndexPath) -> UISwipeActionsConfiguration? {
		let cachedMiniApp = loadedCachedMiniApps[indexPath.row]
		let deleteAction = UIContextualAction(style: .destructive, title: "Borrar", handler: { [weak self] _, _, completion in
			var success = true
			do {
				try GXMiniAppsManager.removeCachedMiniApp(id: cachedMiniApp.miniAppId, version: cachedMiniApp.miniAppVersion)
				completion(true)
			}
			catch let error {
				success = false
				/// Handle error
				print(error.localizedDescription)
			}
			DispatchQueue.main.async { [weak self] in
				self?.reload()
				completion(success)
			}
		})
		return .init(actions: [deleteAction])
	}
}

private class CachedMiniAppTableViewCell: UITableViewCell {
	override init(style: UITableViewCell.CellStyle, reuseIdentifier: String?) {
		super.init(style: .subtitle, reuseIdentifier: reuseIdentifier)
	}
	
	required init?(coder: NSCoder) {
		super.init(coder: coder)
	}
	
	var cachedMiniApp: GXCachedMiniApp! {
		didSet {
			textLabel?.text = cachedMiniApp.miniAppId
			let dateFormatter = DateFormatter()
			dateFormatter.dateStyle = .short
			dateFormatter.timeStyle = .medium
			detailTextLabel?.numberOfLines = 0
			detailTextLabel?.text = """
				Creación: \(dateFormatter.string(from: cachedMiniApp.creationDatetime))
				Último uso: \(dateFormatter.string(from: cachedMiniApp.lastUsedDatetime))
				"""
		}
	}
}
