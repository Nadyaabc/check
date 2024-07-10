## Check Runner
This is a Java program that functions as a check runner. It takes command-line arguments, processes them, and generates a check based on the input.
The check contains a list of products, their amount and price, and, if a buyer has a discount card, info about the discount.

## Prerequisites
* Java Development Kit (JDK) installed on your machine
* Command-line interface (terminal, command prompt, etc.)

## HOW TO START
1. Clone the repository or download the source code files to your local machine.
2. 
`git clone https://github.com/Nadyaabc/check.git`
2. Open your command-line interface and navigate to the project directory.

`cd check-runner`
3. Compile the Java source code.

`javac ./src/main/java/ru/clevertec/check/CheckRunner.java`

## HOW TO USE
To run the program, use the following command:

`java -cp src ./src/main/java/ru/clevertec/check/CheckRunner.java id-quantity discountCard=xxxx balanceDebitCard=xxxx`
where

`id` - product identifier

`quantity` - required quantity

`discountCard=xxxx` -  number of the discount card where xxxx is a four-digit card number

`balanceDebitCard=xxxx` - the balance on the debit card, a floating-point number with or without decimal places.
### example
`java -cp ./src/main/java ru.clevertec.check.CheckRunner 3-1 2-5 5-1 discountCard=1111 balanceDebitCard=100`

## IMPORTANT 
* All arguments should be separated by spaces.
* At least one id-quantity pair should be present in the parameters.
* The discountCard=xxxx argument is optional.
* The balanceDebitCard=xxxx argument is mandatory and the balance can be a positive or negative number.
* Information about discount cards and products is in .csv files `"./src/main/resources/products.csv"` and `"./src/main/resources//discountCards.csv" `
## OUTPUT
After running the program, a file named result.csv will be generated in the project directory. This file will contain the check details, including the total price, discount, and total with discount. Besides, all the information is duplicated to the console.