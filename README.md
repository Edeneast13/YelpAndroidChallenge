# Weedmaps Android Code Challenge!

Hi there!  Thank you for taking the time to conduct the Weedmaps Android code challenge.  Please use this as a foundation to help you save time setting up your workspace; this project already contains some common dependencies and frameworks used in most Android projects.  
**If there are other dependencies and/or frameworks that you'd like to introduce/use please feel free to add them!**

# Dependencies
 - Kotlin Ext
 - Kotlin Coroutines
 - Mockito Kotlin
 - Koin
 - Retrofit
 - Moshi
 - Glide

## First Things First
Register and get a token for the Yelp Fusion API [https://www.yelp.com/developers/documentation/v3/authentication](https://www.yelp.com/developers/documentation/v3)

## Requirements

 1. You will be creating a yelp clone with the designs [here](https://www.figma.com/file/vcfmVmKtPf4hPwIm12jfQ5/Android-Homework?node-id=2%3A9). The data will be coming from the Yelp api linked below. You should return businesses based on the user's input in the search field. 
 2. Each business item in the list should have: **name of the business**, **an image of the business**, and **the top review of the business(reference [here](https://www.yelp.com/developers/documentation/v3/business_reviews) for how to get the business review)**.
 
 ### If you are apply for a senior role, you must also: 
 
 3. The list must endlessly scroll based on the API's paging functionality.
 4. Include Unit Tests for parts of your code with important functionality (no need to get 100% code coverage on the whole project).
 

## If you are applying for a senior role, please do at least 2 of the following:
 1. Requesting the user's location and sending that up in the request
 2. Save the list of a user's recent searchers and display those to the user as autocomplete suggestions
 3. Provide integration tests that verify the behavior established above
 4. Provide caching of the api requests which is invalidated after 15 days

## FAQ:
 - What if a link is broken above? Please reach out and we'll get those fixed. 
 - What if I have any questions? Pretend your PM is on vacation, please use your best judgement and be able to speak as to why you made certain decisions.
 - Can I modify the designs? Yes, within reason. Stay as true to them as you can, and be prepared to explain any changes you make
 - Can I use other/additional libraries that aren't listed? Yes, just be ready to talk about them 

## Notes

 - **Important:** The Fusion API has a **query-per-second limit of 5**. Keep this in mind when configuring your API requests, otherwise you’ll receive a 429 HTTP error code for some of your requests. It would be a good idea to throttle the speed of your subsequent network requests.

## Links

 - [Yelp Fusion API Documentation](https://www.yelp.com/developers/documentation/v3)

## Project Submission Instructions

Please submit either of the following to the link from the email containing your coding HW:
A) your completed .zip file 
B) the link to a public repo with your completed project

