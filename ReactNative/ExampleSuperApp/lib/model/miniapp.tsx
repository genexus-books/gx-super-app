class MiniApp {
  id: string;
  name: string;
  description: string;
  iconUrl: string;
  bannerUrl: string;
  cardUrl: string;
  metadataRemoteUrl: string;
  appEntry: string;
  apiUri: string;
  signature: string;
  version: number;
  creationDate: Date;
  lastUsedDate: Date;

  constructor(json: any) {
    this.id = json[MiniApp.FIELD_ID] || "";
    this.name = json[MiniApp.FIELD_NAME] || "";
    this.description = json[MiniApp.FIELD_DESCRIPTION] || "";
    this.iconUrl = json[MiniApp.FIELD_ICON] || "";
    this.bannerUrl = json[MiniApp.FIELD_BANNER] || "";
    this.cardUrl = json[MiniApp.FIELD_CARD] || "";
    this.metadataRemoteUrl = json[MiniApp.FIELD_METADATA] || "";
    this.appEntry = json[MiniApp.FIELD_ENTRY_POINT] || "";
    this.apiUri = json[MiniApp.FIELD_SERVICES_URL] || "";
    this.signature = json[MiniApp.FIELD_SIGNATURE] || "";
    this.version = json[MiniApp.FIELD_VERSION] || 0;
    this.creationDate = json[MiniApp.FIELD_CREATION_DATE]
      ? new Date(json[MiniApp.FIELD_CREATION_DATE])
      : new Date();
    this.lastUsedDate = json[MiniApp.FIELD_LAST_USED_DATE]
      ? new Date(json[MiniApp.FIELD_LAST_USED_DATE])
      : new Date();
  }

  static fromJson(json: any): MiniApp {
    return new MiniApp(json);
  }

  toJson(): any {
    return {
      [MiniApp.FIELD_ID]: this.id,
      [MiniApp.FIELD_NAME]: this.name,
      [MiniApp.FIELD_DESCRIPTION]: this.description,
      [MiniApp.FIELD_ICON]: this.iconUrl,
      [MiniApp.FIELD_BANNER]: this.bannerUrl,
      [MiniApp.FIELD_CARD]: this.cardUrl,
      [MiniApp.FIELD_METADATA]: this.metadataRemoteUrl,
      [MiniApp.FIELD_ENTRY_POINT]: this.appEntry,
      [MiniApp.FIELD_SERVICES_URL]: this.apiUri,
      [MiniApp.FIELD_SIGNATURE]: this.signature,
      [MiniApp.FIELD_VERSION]: this.version,
      [MiniApp.FIELD_CREATION_DATE]: this.creationDate.toString(),
      [MiniApp.FIELD_LAST_USED_DATE]: this.lastUsedDate.toString(),
    };
  }

  static readonly FIELD_ID = "Id";
  static readonly FIELD_NAME = "Name";
  static readonly FIELD_DESCRIPTION = "Description";
  static readonly FIELD_ICON = "Icon";
  static readonly FIELD_BANNER = "Banner";
  static readonly FIELD_CARD = "Card";
  static readonly FIELD_METADATA = "Metadata";
  static readonly FIELD_ENTRY_POINT = "EntryPoint";
  static readonly FIELD_SERVICES_URL = "ServicesURL";
  static readonly FIELD_SIGNATURE = "Signature";
  static readonly FIELD_VERSION = "Version";
  static readonly FIELD_CREATION_DATE = "CreationDatetime";
  static readonly FIELD_LAST_USED_DATE = "LastUsedDatetime";
}

export default MiniApp;
