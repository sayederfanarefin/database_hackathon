
    var isFullScreen = 0;

    function adjustClockPosition(considerSidebar){
    console.log("adjustClockPosition()....")
    var dynamicDiv = document.getElementById('centeredDivx');
    var divWidth = dynamicDiv.offsetWidth;
    var marginLeft = 0;

    if(considerSidebar){

    var containerWidth = document.getElementById('content-wrapper').offsetWidth;
    var sidebarxxWidth = document.getElementById('accordionSidebar').offsetWidth;
    marginLeft = (containerWidth / 2) - (divWidth / 2) + (sidebarxxWidth/2);

    } else {
    var windowWidth = window.innerWidth || document.documentElement.clientWidth || document.body.clientWidth;
    marginLeft = (windowWidth / 2) - (divWidth / 2);

    }

        marginLeft = marginLeft +marginLeft;

    console.log("Calculated Margin: "+ marginLeft);
    dynamicDiv.style.marginLeft = marginLeft + 'px';


    }




    window.addEventListener('DOMContentLoaded', function () {
    var fullscreenButton = document.getElementById('fullscreen-button');

    // Add a click event listener to the button element
    fullscreenButton.addEventListener('click', function() {
    // Call your function here
    console.log("----------- full recalc");
    if (isFullScreen == 0){
    adjustClockPosition(false);
    isFullScreen = 1;
    } else {
    adjustClockPosition(true);
    isFullScreen = 0;
    }

    });

    adjustClockPosition(true);
    });
