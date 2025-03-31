# Crypto Wallet

A simple Android app to display Ethereum price information using the CoinMarketCap API.

## Features

- Displays current Ethereum price in USD
- Shows 24-hour price change percentage (green for positive, red for negative)
- Updates timestamp
- Refresh button to fetch latest price data

## Setup

### API Key Configuration

This app requires a CoinMarketCap API key. To set up your API key:

1. Get a free API key from [CoinMarketCap](https://coinmarketcap.com/api/)

2. Configure the API key using one of these methods:

   **Option 1: .env File (Recommended)**

   The project includes a `.env` file in the root directory. Replace the placeholder value with your actual API key:
   
   ```
   COINMARKETCAP_API_KEY="your-actual-api-key-here"
   ```
   
   Note: The `.env` file is included in `.gitignore` to prevent accidentally committing your API key.

   **Option 2: System Environment Variable**

   Set an environment variable `COINMARKETCAP_API_KEY` with your API key:
   
   ```bash
   export COINMARKETCAP_API_KEY="your-api-key-here"
   ```

   **Option 3: Gradle Properties**

   Add the API key to your `local.properties` file (do not commit this file to version control):
   
   ```properties
   COINMARKETCAP_API_KEY="your-api-key-here"
   ```

3. Sync your project in Android Studio

## Building the Application

1. Clone the repository
2. Configure your API key as described above
3. Open the project in Android Studio
4. Build and run the app

## Technologies Used

- Kotlin
- Retrofit for network requests
- Gson for JSON parsing
- Coroutines for asynchronous operations
- MVVM architecture pattern
- CoinMarketCap API for cryptocurrency data

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Security Note

The API key configuration used in this project is for educational purposes. In a production app, you would want to implement additional security measures, such as:

- Using the [Secrets Gradle Plugin](https://github.com/google/secrets-gradle-plugin)
- Server-side proxying of API requests to avoid exposing keys in client-side code
- Implementing proper API key rotation and monitoring