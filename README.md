# Check-That-Bike
My final year project

Mind My Bike is an android application which stores information about user’s bikes. This application will help users to recover their bikes if they go missing by:
•	Generating theft report 
•	Provide locations of Garda stations
•	Filter through advertisement websites
•	Compare picture of the lost bike to ones scraped from the website
•	Showing Black Spots

When bike goes missing, user can almost immediately start basic recovery actions which could increase the chances of recovering the bike. With few clicks of the buttons user will be able to generate full theft report which will include bike and the owner information, the bike theft location including the date and time of the theft. This report can be given to the Garda which could speed up the investigation. Then bike details will be used to find similar matches on the advertisement websites such as DoneDeal.ie and Adverts.ie through web scraping and image processing. Data will be scraped from the two websites and stored in the database. This data will be used to find the closest match for users stolen bikes by first finding the bikes with similar brand, then by comparing the image description of the users bikes with the images from the adds. The image description will be generated with the use of the CloudSight image labelling API which provides accurate description of the objects located in the image.
