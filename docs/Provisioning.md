# Mini App Center

The provisioning component is an essential part of the Super App and Mini App's architecture. It's where the Super App is declared and the Mini App's definitions are stored. 

The Mini App Center has the following functionalities:

- Access management for Super App and Mini App publications.
- The Super App owner defines who can upload Mini Apps and to which Super App.
- It provides the digital signatures to be included in the Super App's packaging.
- When publishing a new Mini App (or a new version of an existing one), the metadata is automatically signed with the Super App's private key. The process is:
	1. Register a Mini App/version for the Super App/version
	2. Upload the Mini App package for review
	3. Publish the Mini App as available

See [here](https://wiki.genexus.com/commwiki/wiki?53318,Upload+a+Mini+App+version) for more details.

- Provide each Super App with the list of its available Mini Apps, through an API. These searches can be done by:
	1. Keywords
	2. Location 
	3. Relevant Mini Apps in a certain period of time
