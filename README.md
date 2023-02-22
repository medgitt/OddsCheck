# OddsCheck

Write a small Java command line application that will display real time odds 
for who will win a specific sport match.

Odds information is available in the public Kambi API which is HTTP REST-based using JSON as data 
interchange format. The single GET resource that will be used within this test is located at 
https://eu-offering.kambicdn.org/offering/v2018/ubse/event/live/open.json (which is part of API 
used by Unibet and other sites).

There are two types of events - Matches and Competitions. Within each "event" element in the 
response, there is a field called "tags" which will contain either "MATCH" or "COMPETITION".
Manually look up the numeric field "id" for one event which has tag MATCH. We will not care 
about competitions in this test.

Write an application which takes the id of any event (match) as argument and as output onto the 
console, prints the event name and current date time along with the "mainBetOffer" odds of that 
event, and then continuously polls the above URL every 10th second and prints out any odds changes 
for the "mainBetOffer" in that event.

Example response from that API resource. This is not the fully returned JSON, the example only 
includes the needed elements and fields to be able to construct the output in this test.
{
    liveEvents:[
        {
            "event": {
                "id": 1006937333,
                "name":"Real Madrid - Barcelona FC",
                "tags": ["MATCH", "OPEN_FOR_LIVE"]
            },
            "mainBetOffer": {
                "id": 2233351359,
                "outcomes": [
                    {
                        "id": 2818435773,
                        "label": "Real Madrid",
                        "odds": 5250
                    },
                    {
                        "id": 2818435774,
                        "label": "Draw",
                        "odds": 3100
                    },
                    {
                        "id": 2818435775,
                        "label": "Barcelona FC",
                        "odds": 1740
                    }
                ]
            }
        }
    ]
}

The odds in the response should be divided by 1 000 and be displayed with two decimals.
The "label" fields in the API response may contain other values like "1", "X", "2" instead of 
team/player names. You may display the label text as-is from the API.

Example run of the application should look like this (continuously until user interrupts the process)

> java -jar OddsCheckerApp.jar 1006937333
Event: Real Madrid - Barcelona FC
[2020-11-06 21:07:20] | Real Madrid:   10.15 | Draw:    6.10 | Barcelona FC:    1.34 |
[2020-11-06 21:08:10] | Real Madrid:    5.30 | Draw:    3.15 | Barcelona FC:    1.70 |
[2020-11-06 21:08:30] | Real Madrid:    5.25 | Draw:    3.10 | Barcelona FC:    1.74 |


While the programming is running, you can in a browser go to 
https://www.unibet.se/betting/sports/event/live/<event-id> to compare the output and odds changes.
