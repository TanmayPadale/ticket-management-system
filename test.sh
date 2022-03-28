for i in {1..100}
do
echo $i
curl --location --request GET 'http://localhost:8080/tms/checkStatus/3' \
--header 'Authorization: Bearer 2-4'
curl --location --request GET 'http://localhost:8080/tms/checkStatus/7' \
--header 'Authorization: Bearer'
curl --location --request GET 'http://localhost:8080/tms/checkStatus/6' \
--header 'Authorization: Bearer 2-7'
done