package com.example.lindsay.delta5.utils

import java.text.SimpleDateFormat
import java.util.*

class DateUtils {
    companion object {
        private const val FORMAT = "yyyy-MM-dd"

        fun format(date: Date): String {
            val dateFormatter = SimpleDateFormat(FORMAT)
            return dateFormatter.format(date)
        }

        fun currentDate(): Long {
            return Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.timeInMillis
        }

        fun getEpochTimeFromString(dateStr: String?): Long {
            if(dateStr.isNullOrBlank()) {
                return -1
            }

            return SimpleDateFormat(FORMAT).parse(dateStr).time
        }

        fun getFormattedStringFromEpochTime(epochTime: Long): String {
            return format(Calendar.getInstance().apply { timeInMillis = epochTime }.time)
        }

        /*
            Takes an epoch time and converts it to our string format and then calculates the days
            from today that date is
         */
        fun daysFrom(toCheck: Long): Int {
            val dateStr = format(Calendar.getInstance().apply { timeInMillis = toCheck }.time)

            return daysFrom(dateStr)
        }

        /*
            Takes our string date format and calculates the days from today that date is
         */
        fun daysFrom(time: String?): Int {
            val date = SimpleDateFormat(FORMAT).parse(time)
            val nextDate = Calendar.getInstance()
            nextDate.time = date

            val today = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            nextDate.apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            var days = 0

            if (today > nextDate) {
                while (nextDate.before(today)) {
                    nextDate.add(Calendar.DAY_OF_YEAR, 1)
                    days -= 1
                }
            } else if (nextDate > today) {
                while (today.before(nextDate)) {
                    today.add(Calendar.DAY_OF_YEAR, 1)
                    days += 1
                }
            }

            return days
        }

        fun distanceInDays(day1Time: Long, day2Time: Long): Int {
            val firstDay = Calendar.getInstance().apply {
                timeInMillis = day1Time
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            val secondDay = Calendar.getInstance().apply {
                timeInMillis = day2Time
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            var days = 0

            if (firstDay > secondDay) {
                while (secondDay.before(firstDay)) {
                    secondDay.add(Calendar.DAY_OF_YEAR, 1)
                    days -= 1
                }
            } else if (secondDay > firstDay) {
                while (firstDay.before(secondDay)) {
                    firstDay.add(Calendar.DAY_OF_YEAR, 1)
                    days += 1
                }
            }

            return days
        }

        fun addDaysToToday(daysToAdd: Int): Long {
            val cal = Calendar.getInstance().apply {

                // Set it to be midnight of the current day
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)

                // Add daysToAdd amount of days to the date
                add(Calendar.DAY_OF_YEAR, daysToAdd)
            }

            return cal.timeInMillis
        }

        fun addDaysToDate(days: Int, epochTime: Long?): Long {
            var calendar = Calendar.getInstance().apply {
                if(epochTime != null) {
                    timeInMillis = epochTime
                }

                add(Calendar.DAY_OF_YEAR, days)

                // Set it to be midnight of the current day
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)

            }

            return calendar.timeInMillis
        }

        /*
            The next few functions are to properly parse the dates we store in Realm. Since we
            store them as a string and not as a datetime or something like that, we need to make sure
            that we are getting each section properly
         */
        fun getHourFromDateString(dateStr: String): Int {
            // We save the dates in 12-hour format
            var hour = dateStr.split(":")[0].toInt()

            // After we get the hour (12-11) we need to figure out if it is am or pm and convert it
            //  to 24 hour
            val amPm = getAmPmFromDateString(dateStr)

            if(amPm == "PM" && hour != 12) {
                hour += 12
            } else if(amPm == "AM" && hour == 12) {
                hour = 0
            }
            return hour
        }

        fun getMinuteFromDateString(dateStr: String): Int {
            return dateStr.split(":")[1].substring(0, 2).toInt()
        }

        fun getAmPmFromDateString(dateStr: String): String {
            return dateStr.split(":")[1].substring( 3)
        }
    }
}