/*
 * connction with singleton to database
 */
public class Connection {

    private static Connection instance;
    private static WatchDAO watchDao;
    
    private init() {
        let appDelegate = UIApplication.shared.delegate as! AppDelegate
        let ctx = appDelegate.persistentContainer.viewContext
        let rsql: NSFetchRequest<SynchroniseurMO> = SynchroniseurMO.fetchRequest()
        if let syncrMO = try? ctx.fetch(rsql).first {
            Synchroniseur0.syncMO = syncrMO
        }
    }
    
    public static func getSynchroniseur() -> SynchroniseurMO {
        if Synchroniseur0.instance == nil {
            Synchroniseur0.instance = Synchroniseur0()
        }
        return Synchroniseur0.syncMO!
    }
    
    /// sauvegarde les dernières valeurs dans le coreData
    public static func save(){
        let appDelegate = UIApplication.shared.delegate as! AppDelegate
        let ctx = appDelegate.persistentContainer.viewContext
        let rsql: NSFetchRequest<SynchroniseurMO> = SynchroniseurMO.fetchRequest()
        do {
            try ctx.save()
        } catch {
            print("erreur : \(error.localizedDescription)")
        }
    }
    
}
