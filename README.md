# Stellar Hardware Wallet Key Server 

This project demonstrates how to securely store the public key / secret seed associated with a Stellar account.  
 
The public key is stored as a hash and the secret seed is stored as encrytpted ciphertext in an embedded database. Neither key is _ever_ exposed as plaintext.

Web services implement the following functions:
- **store keys** -  Store the account keys associated with a specific Stellar account.
- **get keys** - Retrieve the account keys associated with a specific Stellar account.
- **remove keys** - Remove the account keys associated with a specific Stellar account.

Also see the [Stellar Hardware Wallet Angular App](https://github.com/programming4phone/StellarHardwareWalletNgApp "Stellar Hardware Wallet Angular App") project.

For further details about Stellar accounts see <https://www.stellar.org/developers/guides/get-started/create-account.html>.

## Development stack

This project was developed using Java 8, Spring Boot, Spring Data JPA, HSQLDB, Eclipse Oxygen, and Maven. 

## Prerequisites

The project requires a predefined HSQLDB database. The database must reside in a folder named `db/wallet.temp`.

## Build

Prior to building, the Angular compiled code located in the `dist/` directory needs to be copied into this project's `resources\static` directory.

Run `mvn clean install` to build the project and run the supplied integration tests. The build artifacts will be stored in the `target/` directory. 

## Running the Spring Boot container
Change to the source code directory and run `java -jar target/wallet.api-0.0.1-POC.jar`.

## Flash Drive Image
The final executable from this project is meant to be run from a flash drive. This allows the application to run in total isolation without accessing the internet.

The format of the files on the flash drive E: are as follows:

`E:`
`-- wallet`
`---- db`
`------ wallet.tmp`
`------ wallet.lck`
`------ wallet.log`
`------ wallet.properties`
`------ wallet.script`
`---- jre`
`------ bin`
`------ lib`
`-- hardware.wallet-0.0.1-POC.jar`
`-- runWallet.cmd`

The `db` directory containing an initialized HSQLDB database is provided with this project.
The `jre` directory comes from a local installation of the Java SDK. For example, it is a copy of the folder `C:\Program Files\Java\jre1.8.0_162` renamed to `jre`.
