# VoucherVault Mobile

Applicazione Android frontend per VoucherVault, ispirata al design di Klarna. L'app mostra tutte le carte fedeltà salvate, consente la visualizzazione ad elenco o griglia con icone dei negozi, permette di aggiungere nuove carte scansionando i barcode e mostra un dettaglio con codice a barre e valore numerico quando si seleziona una carta.

## Funzionalità principali
- Visualizzazione delle carte in elenco o griglia con switch rapido nell'app bar.
- Gestione locale dello stato delle carte e colori brandizzati per negozio.
- Aggiunta di nuove carte tramite dialog che offre sia inserimento manuale sia scansione barcode (ZXing Embedded).
- Dettaglio carta con anteprima del codice a barre e numero completo.

## Requisiti
- Android Studio Giraffe o superiore.
- SDK Android 34 (minSdk 26).
- JDK 17.

## Build
1. Importa il progetto in Android Studio.
2. Se necessario genera i file del wrapper Gradle eseguendo `gradle wrapper --gradle-version 8.6` (o assicurati di avere `gradle/wrapper/gradle-wrapper.jar`).
3. Avvia l'app su un dispositivo o emulatore con Android 8.0+.
