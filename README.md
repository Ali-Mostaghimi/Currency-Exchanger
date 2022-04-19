# Currency-Exchanger
* Syncing every 5 second with back-end to fetch fresh currency rates
* Handling lifecycle of flow and coroutine for stop syncing when app goes in the background and starting syncing when comes in foreground
* Handling loading, error, success state of api call and notify user about these states with option to retry
* Providing a way to calculate commissions dynamically with different strategies (solved with design patterns)
* Still works even when currencies added in the server side and this currency also will be add in the application dynamically
* Notifies user about every faild transaction with different senarioes
* Flexible wallet with alot of currencies
* Wrote in Clean-MVVM architecture to be scalable, flexible, clean and easy to maintain

# Libraries and Tools
* Kotlin
* Coroutines and Flow
* Room Database
* Retrofit and Okhttp
* Dagger2

# Screen-Shots
<p> <img height="450" src="https://user-images.githubusercontent.com/52744015/164037933-eaddde99-384c-409c-9319-89a842e9169b.jpg" border="5" />
<img height="450" src="https://user-images.githubusercontent.com/52744015/164038199-1e8be482-c63a-4ff6-9412-ceb2ef146368.jpg" border="1"/>
<img height="450" src="https://user-images.githubusercontent.com/52744015/164038375-b4b59587-a29d-4f3f-a97b-84a2b15f7062.jpg" border="1"/> </p>
<p> <img height="450" src="https://user-images.githubusercontent.com/52744015/164038521-6a40d876-af8f-4fd0-81fe-064e92fcbf92.jpg" border="1"/>
<img height="450" src="https://user-images.githubusercontent.com/52744015/164038661-0e195618-cb27-4f7a-b862-ffbce8be6535.jpg" border="1"/>
<img height="450" src="https://user-images.githubusercontent.com/52744015/164038853-c235fa60-8722-4548-8af1-c2b9a8d838e4.jpg" border="1"/> </p>
