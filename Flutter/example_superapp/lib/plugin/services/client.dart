class ClientsService {
  static const sdtClient = "Client";
  static const fieldName = "name";
  static const fieldLastName = "lastName";
  static const fieldEmail = "email";
  static const fieldId = "clientId";

  static Map<String, String> getClient(String clientId) {
    return {
      fieldName: "Juan",
      fieldLastName: "Perez",
      fieldEmail: "jperez@example.com",
      fieldId: clientId
    };
  }
}
