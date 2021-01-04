# HTTP API for varer:
* Et enkelt API for å sende og motta HTTP requests til databasen som benyttes i [Kassesystem](https://github.com/h577870/Kassesystem).
* Planen videre er å bygge en mobilapplikasjon ved bruk av Kotlin/Multiplatform, som skal benytte dette API-et.
* Applikasjonen er bygget med Gradle/Kotlin DSL.

## Bibliotek og rammeverk:
* Ktor:
    - Enkelt rammeverk som håndterer HTTP requests.
* Exposed:
    - ORM-rammeverk.
* Hikari:
    - Forbindelse med database.
* Netty:
    - API-et kjører på en Netty-server.
  
## Litt om JWT og serversesjon:
  * Bruker logger inn, og dersom suksess, sender jwt-token i body til brukeragent.
  * Brukeragent legger denne inn som header i fremtidige requests til server, slik:
    - {"Authorization":"Bearer \<sett inn token her>"}
  * Serversesjon sendes som http-only cookie, og benyttes ikke av brukeragent.