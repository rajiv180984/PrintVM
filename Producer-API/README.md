## Swagger API URL
http://localhost:8185/jioutils/swagger-ui.htm

## Response of PrintRequest API:

{
  "docId": "f1612e4d-3569-4f40-9845-0b9184cdffb1",
  "machineId": "8287",
  "custId": "ecaa328d-17e3-4b66-9ec3-da26ff6283d2",
  "documentURL": "s3://zeroxvm/8782/samplechapterGTA.pdf",
  "mobileNo": "9988551218",
  "emailId": "rathor.rajeev@gmail.com",
  "printProperties": {
    "pageCount": 4
  },
  "securityCode": "test",
  "printCost": 10.239999771118164,
  "promocode": "zero200",
  "paymentStatus": "COMPLETED",
  "balanceAmountToPay": 0,
  "createOn": 0,
  "requestType": "DIRECT",
  "businessType": "PARTNER"
}

## Upload Public Document

{"machineList":["8283"],
	"category":"Account",
	"SubCategory":"New Account",
	"Name":"Form-2",
	"desc":"new account",
	"isPublic":true
	}

## Machine Registration

{
  "businessType": "PARTNER",
  "careTakerId": "9897954387",
  "cityName": "CV Raman Nagar",
  "copierId": "string",
  "geoLocation": {
    "coordinates": [
      27.280256, 77.940646],
    "type": "POINT"
  },
  "machineAddress": "string",
  "machineId": "82879",
  "machineName": "string",
  "printerType": "string",
"partnerId":"9988551217",
  "qrcodeEnable": "string",
  "qrCodeId": "1345",
  "respberryDeviceId": "string",
  "scannerId": "string",
"machineBelongsTo":"ZEROXVM"
}

## Register Customer
{
  "address": "Agra",
  "cityName": "Agra",
  "custName": "Ganesh",
  "education": "Phd",
  "emailId": "ganesh1@gmail.com",
  "mobileNo": "9988551218",
	"userCredentials": {
    "password": "testuser",
    "userName": "ganesh1@gmail.com",
   
  }

}

{
  "address": "string",
  "cityName": "string",
  "custId": "string",
  "custName": "Ganesh",
  "education": "B.Tech",
  "emailId": "rajiv180984@gmail.com",
  "mobileNo": "9988551218",
  "pinCode": "205135",
  "profession": "string",
  "stateName": "string",
  "userCredentials": {
    "password": "Indian",
    "userName": "shriganesh"
    
  }
}


## Register Partner #########
{
  "emailId": "partner1@gmail.com",
  "machineId": "8281",
  "partnerAddress": "Agra",
  "partnerMobileNo": "9988551217",
  "partnerName": "Test Partner",
  "partnerPaymentDetails": {},
  "partnerPrintStats": {}
}

## Caretaker Registration

{
  "address": "Taj Nagari 2",
  "adharCardNumber": "99885512347986",
  "email": "rathor.rajeev@gmail.com",
  "location": "Agra",
  "mobile": "9988551218",
  "mobile2": "8095507292",
  "name": "Rajeev test",
  "payment": "string",
  "status": "ACTIVE"
}

## QRCode Collection 
{
"qrCodeId":"1234",
"qrCodeImagePath":"S3://xeroxvm/",
"qrcodeGeneratorURL":"http://qrcodegenerator",
"qrcodeWifiText":"shriGanesh",
"machineId":"82879",
"createOn":""
}


Sent OTP
https://2factor.in/API/V1/0188b88c-0a2d-11ea-9fa5-0200cd936042/SMS/8904668406/AUTOGEN

https://2factor.in/API/V1/{api_key}/SMS/{phone_number}/AUTOGEN

https://2factor.in/API/V1/0188b88c-0a2d-11ea-9fa5-0200cd936042/SMS/VERIFY/290a3a31-b024-4812-b015-da31cb527cb8/796491

{"Status":"Success","Details":"290a3a31-b024-4812-b015-da31cb527cb8"}

Payment Data:

{
  "orderId": "eyu134",
  "custId": "user1",
  "mobileNo": "8904668406",
  "emailId": "rathor.rajeev@gmail.com",
  "txnAmount": "20",
  "jobData": {
    "fileName": "sdfjk",
    "officeName": "Real-State",
    "count": 10
  }
}


http://localhost:8185/zerox/first/dream/getallnearbyprinter

{
    "coordinates": [
      27.280256, 77.940646],
    "type": "POINT"
  }
  
 #### Load Public Doc
 
 {   "machineId": "8782",   "machineList": ["8782","8282"],   "mobileNo": "9988551218",   "category":"EDU",   "SubCategory":"ECE",   "Name": "Digital",   "isPublic":true, "ownerEmailId":"rathor.rajeev@gmail.com" }
## Check wallet Balance
{
"emailId":"testUser@mailinator.com",
"mobileNo": "9876543211"
}

## Payment history
{
  "emailId": "rajiv180984@gmail.com",
  "mobileNo": "8904668406",
  "wid": "ytyrb-87kgk-kkgte-tybkl-gsncb",
  "orderData": {
    "orderId": "",
    "amount": "",
    "currency": "",
    "receipt": "",
    "payment_capture": "",
    "createdAt": ""
  },
  "transactionData": {
    "transId": "gksnt-67dk-8kljv-hjg780d",
    "tax": 1
  }
}

## print Props ##
{"machineId":"",
"mobileNo":"",
"emailId":"",
"docId":"",
"printProperties":{
"pageCount":"20",
"pexpn":"2,5,6",
"color": false,
"pSize":"A4"

}
}



