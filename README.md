# Möle
Early Skin Cancer Detection using Machine Learning Image Processing Models
### DeltaHacks 2019

## Inspiration
Each year, more people are diagnosed with skin cancer than all other cancer's combined. It is also estimated that one in five people will develop skin cancer by the time they reach 70 years of age. Skin cancer is also one of the easiest to cure, if detected early. With this in mind, we were inspired to develop a solution that empowers users to analyze and monitor the health of their skin.

## What it does
Möle allows users to take pictures of freckles, blemishes and moles on their skin. The application then uses a Machine Learning image processing model to compare it to more than ten thousand images of various skin conditions. The resulting analysis informs the user whether the blemish is benign or malignant, and if it resembles Melanoma, Bowens Disease, Basal Cell Carcinoma, Benign lesions, Dermatofibroma, or Melanocytic Nevi.

The application also allows users to track blemishes over time, providing updates on their health as they develop and change.

## How we built it
Möle is built for Android devices, using Kotlin/Java and a Realm database. The image processing agent was built using Azure's Custom Vision Model, trained using HAM10000, a neural network training dataset published in the Harvard Dataverse, with over ten thousand images of common skin lesions.

For more information on our dataset, please visit: https://bit.ly/2UlvD7P

## Challenges we ran into
All team members were unfamiliar with the projects technical stack at the beginning of this competition. The learning curve, coupled with the limited development time, proved to be our biggest challenge. Sorting, tagging and uploading over 10 000 images for our computer vision model was also a challenge on its own, which required a fair amount of scripting.

## Accomplishments that we're proud of
All members of our team undertook a large part of this project, working with tech we were not familiar with, and managed to complete a project within 24 hours! On top of this, we feel that the product we created could be used to help educate and inform millions of people worldwide, who may not have access to dermatologists or skin care professionals.

## What we learned
We would struggle to fit everything we learned over the past 24 hours onto one page, but we feel the biggest lessons were that...

* To always focus on the minimum viable product when time is limited
* It's more fun to work on projects you're excited about
* Wear more sunscreen, skin care is no joke!

## What's next for Möle
Training our model with larger datasets to improve accuracy, and fine tuning the interface to create a more seamless user experience. We also intend to better integrate users age, sex, and geographical location into the analysis in order to produce more accurate results.
