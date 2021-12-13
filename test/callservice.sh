#!/bin/bash

json={"\"query\"":"\"SELECT\u0020Country,\u0020AVG(Order_Processing_Time),\u0020MAX(Units_Sold),\u0020MIN(Units_Sold),\u0020SUM(Total_Revenue),\u0020COUNT(Order_ID)\u0020FROM\u00201500000SalesRecords_processed\u0020WHERE\u0020Region='Australia\u0020and\u0020Oceania'\u0020AND\u0020Item_Type='Office\u0020Supplies'\u0020GROUP\u0020BY\u0020Country;\""}

echo "Invoking Lambda function using AWS CLI"
time output=`aws lambda invoke --invocation-type RequestResponse --function-name service3aurora --region us-east-2 --payload $json /dev/stdout | head -n 1 | head -c -2 ; echo`

echo ""
echo "JSON RESULT:"
echo $output | jq
echo ""
