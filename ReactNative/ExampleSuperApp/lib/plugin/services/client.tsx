class ClientsService {
    static sdtClient = "Client";
    static fieldName = "name";
    static fieldLastName = "lastName";
    static fieldEmail = "email";
    static fieldId = "clientId";
  
    static getClient(clientId: string) {
      return {
        [ClientsService.fieldName]: "Juan",
        [ClientsService.fieldLastName]: "Perez",
        [ClientsService.fieldEmail]: "jperez@example.com",
        [ClientsService.fieldId]: clientId
      };
    }
  }
  
  export default ClientsService;