# Stellar Hardware Wallet Key Server 

This project demonstrates how to securely store a public key and secret seed associated with a Stellar account. Since this codebase is in the "proof of concept" stage, all access is restricted to the Stellar test network.
 
Account keys are stored in an embedded database. Keys are obfuscated, either through hashing or encryption, prior to being stored.

Web services implement the following functions:
- **store keys** -  Store the account keys associated with a specific Stellar account.
- **get keys** - Retrieve the account keys associated with a specific Stellar account.
- **remove keys** - Remove the account keys associated with a specific Stellar account.

Also see the [Stellar Hardware Wallet Angular App](https://github.com/programming4phone/StellarHardwareWalletNgApp "Stellar Hardware Wallet Angular App") project.

For further details about Stellar accounts see <https://www.stellar.org/developers/guides/get-started/create-account.html>.

## Development stack

This project was developed using Java 8, Spring Boot, Spring Data JPA, HSQLDB, Eclipse Oxygen, and Maven. 

## Prerequisites

The project requires a predefined HSQLDB database. The database must reside in a folder named `db/wallet.temp`. It is include within this project's codebase.

## Build

Prior to building, the Angular compiled code, from the Stellar Hardware Wallet Angular App project, located in the `dist/` directory needs to be copied into this project's `resources/static` directory.

Run `mvn clean install` to build the project and run the supplied integration tests. The build artifacts will be stored in the `target/` directory. 

## Running the Spring Boot container
Change to the source code directory and run `java -jar target/wallet.api-0.0.1-POC.jar`.

## Flash Drive Image
The final executable from this project is targeted to run from a flash drive. This allows the application to run in total isolation without directly accessing the internet.

The format of the files on the flash drive E: are as follows:

`E:`<br/>
`-- wallet`<br/>
`---- db`<br/>
`------ wallet.tmp`<br/>
`------ wallet.lck`<br/>
`------ wallet.log`<br/>
`------ wallet.properties`<br/>
`------ wallet.script`<br/>
`---- jre`<br/>
`------ bin`<br/>
`------ lib`<br/>
`-- hardware.wallet-0.0.1-POC.jar`<br/>
`-- runWallet.cmd`<br/>

The `db` directory containing an initialized HSQLDB database is provided with this project.
The `jre` directory comes from a local installation of the Java SDK. For example, it is a copy of the folder `C:\Program Files\Java\jre1.8.0_162` renamed to `jre`.

To run from the flash drive (assumes E: drive), enter the following commands from the Windows Command prompt:<br/>
`E:`<br/>
`cd wallet`<br/>
`runWallet.cmd`<br/>

Once started the micro service is started it can be accessed from any browser using this link `http:\\localhost:5775`. Use Ctrl^c to terminate the micro service.