(function ($) {
    "use strict";

    $.fn.extend({ 

      countdown100: function(options) {
        var defaults = {
          timeZone: "",
          endtimeYear: 0,
          endtimeMonth: 0,
          endtimeDate: 0,
          endtimeHours: 0,
          endtimeMinutes: 0,
          endtimeSeconds: 0,
        }

        // console.log("Options from countdown.js");
        // console.log(options);
        var options =  $.extend(defaults, options);
        // console.log(options);

        return this.each(function() {
          var obj = $(this);
          var timeNow = new Date();

          var tZ = options.timeZone; console.log(tZ);
          var endYear = options.endtimeYear; console.log(endYear);
          var endMonth = options.endtimeMonth;console.log(endMonth);
          var endDate = options.endtimeDate;console.log(endDate);
          var endHours = options.endtimeHours;console.log(endHours);
          var endMinutes = options.endtimeMinutes;console.log(endMinutes);
          var endSeconds = options.endtimeSeconds;console.log(endSeconds);

          if(tZ == "") {
            var deadline = new Date(endYear, endMonth - 1, endDate, endHours, endMinutes, endSeconds);
          } 
          else {
            var deadline = moment.tz([endYear, endMonth - 1, endDate, endHours, endMinutes, endSeconds], tZ).format();
          }

          if(Date.parse(deadline) < Date.parse(timeNow)) {
            var deadline = new Date(Date.parse(new Date()) + endDate * 24 * 60 * 60 * 1000 + endHours * 60 * 60 * 1000); 
          }
          
          var t = Date.parse(deadline) - Date.parse(new Date());
            
          var clock = $(obj).FlipClock(t/1000, {
            clockFace: 'DailyCounter',
            countdown: true
          });


        });
      }
    });

    

})(jQuery);