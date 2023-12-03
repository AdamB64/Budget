Adam Buchan 2107616
Budget app
a app that allow the user to manage their budget
this a budget app developed in android studio that has the function of registering and logging in a user and then letting them add income and expenses with the amount date and description if needed and also set thier goal for their budget it also tell them thier budget and how close they are to thier budget

<h1>Details of app design:</h1>
<h1>Start page</h1>
<img src="Screenshot 2023-12-03 130722.png">
this is the main start up page of my app and all it does is tell the user what kind of app this is and allow the user to navigate to the register page or the login page using the buttons.
<h1>Register page</h1>
<img src="Screenshot 2023-12-03 133303.png">
this is the regsiter page that allow the user to make an acount using a user name and an password using the input widgets with some user error handling added in and it make a new object in my firebase databse for the user and with all the object it will need and once you click the regsiter button it takes you to the login page to login there.
<h1>Login page</h1>
<img src="Screenshot 2023-12-03 133319.png">
this is the login page that allow you to login by searching through the databse,using the inputted username and password from the user from the input widgets, to see if a user with the same username and password exist and if it does naviage to the home page and if it doenst it tells the user it can not log in as the ethier the user doesnt exist or the password is wrong.
<h1>Home page</h1>
<img src="Screenshot 2023-12-03 133330.png">
this is the home page that mainly is here as a way to naviagte to all the main pages such income and expenses page and the budget and goals pages.
<h1>Budget page</h1>
<img src="Screenshot 2023-12-03 133337.png">
This is the budget page that allow the user to see thier budget and also to see thier goal taken from the firebase database and output them to a text view widget and the percentage to reaching it and also allow the user to navigate to the income and expenses pages and the goals pages.
<h1>goals page</h1>
<img src="Screenshot 2023-12-03 133349.png">
this is the goals page that allow the user to update thier goal by using a input box widget and posting the new goal to the firebase daytabse and this also allow naviagion to the income and expenses pages and back to the budget page
<h1>income/exepenses page</h1>
<img src="Screenshot 2023-12-03 133356.png">
<img src="Screenshot 2023-12-03 133411.png">
this is the income and expenses pages and it allow the user to input the amount of income/expenses the date gotten and a description,using some input widgets, if wanted and then post the new income/exepenses to the firebase databse when the add income button is clicked and the page also get the value at the time of budget and then adds the new income or takes away the expenses from it and then post that to the databse along with the income/expenses and the page also allow the user to naviagte to the income/expenses view page and back to the budget page
<h1>income/epenses view page</h1>
<img src="Screenshot 2023-12-03 133403.png">
<img src="Screenshot 2023-12-03 133416.png">
these are the income and expenses pages that take all the added income or expenses from the database and put them into a list view so the user can see and allow naviagion to the budget page
