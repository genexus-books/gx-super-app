class MiniApp {
  final String id;
  final String name;
  final String description;
  final String iconUrl;
  final String bannerUrl;
  final String cardUrl;
  final String metadataRemoteUrl;
  final String appEntry;
  final String apiUri;
  final String signature;
  final int version;
  final String type;
  final DateTime creationDate;
  final DateTime lastUsedDate;

  MiniApp.fromJson(Map<String, dynamic> json)
      : id = json.containsKey(FIELD_ID) ? json[FIELD_ID] : "",
        name = json.containsKey(FIELD_NAME) ? json[FIELD_NAME] : "",
        description = json.containsKey(FIELD_DESCRIPTION) ? json[FIELD_DESCRIPTION] : "",
        iconUrl = json.containsKey(FIELD_ICON) ? json[FIELD_ICON] : "",
        bannerUrl = json.containsKey(FIELD_BANNER) ? json[FIELD_BANNER] : "",
        cardUrl = json.containsKey(FIELD_CARD) ? json[FIELD_CARD] : "",
        metadataRemoteUrl = json.containsKey(FIELD_METADATA) ? json[FIELD_METADATA] : "",
        appEntry = json.containsKey(FIELD_ENTRY_POINT) ? json[FIELD_ENTRY_POINT] : "",
        apiUri = json.containsKey(FIELD_SERVICES_URL) ? json[FIELD_SERVICES_URL] : "",
        signature = json.containsKey(FIELD_SIGNATURE) ? json[FIELD_SIGNATURE] : "",
        version = json.containsKey(FIELD_VERSION) ? json[FIELD_VERSION] : 0,
        type = json.containsKey(FIELD_TYPE) ? json[FIELD_TYPE] : "",
        creationDate = json.containsKey(FIELD_CREATION_DATE) ? DateTime.parse(json[FIELD_CREATION_DATE]) : DateTime.now(),
        lastUsedDate = json.containsKey(FIELD_LAST_USED_DATE) ? DateTime.parse(json[FIELD_LAST_USED_DATE]) : DateTime.now();

  Map<String, dynamic> toJson() => {
    FIELD_ID: id,
    FIELD_NAME: name,
    FIELD_DESCRIPTION: description,
    FIELD_ICON: iconUrl,
    FIELD_BANNER: bannerUrl,
    FIELD_CARD: cardUrl,
    FIELD_METADATA: metadataRemoteUrl,
    FIELD_ENTRY_POINT: appEntry,
    FIELD_SERVICES_URL: apiUri,
    FIELD_SIGNATURE: signature,
    FIELD_VERSION: version,
    FIELD_TYPE: type,
    FIELD_CREATION_DATE: creationDate.toString(),
    FIELD_LAST_USED_DATE: lastUsedDate.toString()
  };
  
  static const String FIELD_ID = "Id";
  static const String FIELD_NAME = "Name";
  static const String FIELD_DESCRIPTION = "Description";
  static const String FIELD_ICON = "Icon";
  static const String FIELD_BANNER = "Banner";
  static const String FIELD_CARD = "Card";
  static const String FIELD_METADATA = "Metadata";
  static const String FIELD_ENTRY_POINT = "EntryPoint";
  static const String FIELD_SERVICES_URL = "ServicesURL";
  static const String FIELD_SIGNATURE = "Signature";
  static const String FIELD_VERSION = "Version";
  static const String FIELD_TYPE = "Type";
  static const String FIELD_CREATION_DATE = "CreationDatetime";
  static const String FIELD_LAST_USED_DATE = "LastUsedDatetime";
}
