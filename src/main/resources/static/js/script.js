$(function () {
    var $userRegister = $("#userRegister");

    $userRegister.validate({

        rules:{
            name:{
                required :true,
                lettersonly : true
            },
            email:{
                required :true,
                space: true,
                email:true
            },
            mobileNumber:{
                required :true,
                space: true,
                numericOnly: true,
                minlength: 10,
                maxlength: 12
            },
            password:{
                required :true,
                space: true
            },
            confirmpassword:{
                required :true,
                space: true,
                equalTo:'#pass'
            },
            address:{
                required :true,
                all: true
            },
            city:{
                required :true,

            },
            state:{
                required :true,
            }
        },
        messages:{
            name: {
                required: "Name required",
                lettersonly:"Invalid name"
            },
            mobileNumber:{
                required :'mob no must be required',
                numericOnly : "invalid mob no",
                space: "space not allowed",
                minlength: "min 10 digit",
                maxlength: "max 12 digit"
            },
            email:{
                required :'email name must be required',
                space: 'space not allowed',
                email: 'Invalid email'
            },
            password:{
                required :'password must be required',
                space: "space not allowed"
            },
            confirmpassword:{
                required :'confirm password must be required',
                space: "space not allowed",
                equalTo: 'password mismatch'
            },
            address:{
                required :'address must be required',
                all:"invalid"
            },
            city:{
                required :'city must be required',

            },
            state:{
                required :'state must be required',
            }


        }
    })

    //Booking Validation
    var $bookings=$("#bookings");
    $bookings.validate({

        rules:{
            name:{
                required:true,
                lettersonly:true
            },
            email: {
                required: true,
                space: true,
                email: true
            },
            mobileNumber: {
                required: true,
                space: true,
                numericOnly: true,
                minlength: 10,
                maxlength: 12

            },
            totalDay: {
                required: true

            },

           checkIn: {
                required: true,
                datenow: false

            },
            checkOut: {
                required: true,
                datenow: false
            },
            paymentType:{
                required: true
            },
            room:{
                required:false
            },
            price:{
                required:false
            }
        },
        messages:{
            name:{
                required:'Please enter your name',
                lettersonly:'invalid name'
            },
            email: {
                required: 'Please enter your email address',
                space: 'space not allowed',
                email: 'Invalid email'
            },
            mobileNumber: {
                required: 'Please enter your mobile number',
                space: 'space not allowed',
                numericOnly: 'invalid mob no',
                minlength: 'min 10 digit',
                maxlength: 'max 12 digit'
            }
            ,
            totalDay: {
                required: 'Please enter your total day',

            },
            checkIn: {
                required:'Please select a check in date'
            },
            checkOut: {
                required:'Please select a check out date'
            },
            paymentType:{
                required: 'select payment type'
            }


        }
    })

    //Booking Validation
    var $contacts=$("#contacts");
    $contacts.validate({

        rules:{
            name:{
                required:true,
                lettersonly:true
            },
            email: {
                required: true,
                space: true,
                email: true
            },

            title:{
                required:true
            },
            message:{
                required:true
            }
        },
        messages:{
            name:{
                required:'Please enter your name',
                lettersonly:'invalid name'
            },
            email: {
                required: 'Please enter your email address',
                space: 'space not allowed',
                email: 'Invalid email'
            },

            title: {
                required: 'Please enter your title',

            },
            message: {
                required: 'Please enter your message',

            }


        }
    })






})

jQuery.validator.addMethod('lettersonly',function (value,element){
    return /^[^-\s][a-zA-Z_\s-]+$/.test(value);
});
jQuery.validator.addMethod('space', function(value, element) {
    return /^[^-\s]+$/.test(value);
});

jQuery.validator.addMethod('all', function(value, element) {
    return /^[^-\s][a-zA-Z0-9_,.\s-]+$/.test(value);
});


jQuery.validator.addMethod('numericOnly', function(value, element) {
    return /^[0-9]+$/.test(value);
});

jQuery.validator.addMethod("datenow", function(value, element) {
    const selectedDate = new Date(value);
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    return selectedDate >= today;
}, "Date must be today or later");

