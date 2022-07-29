# Feedback - score: 74

## Overall quality of submission is very good.

Your implementation is pretty impressive. Code is well structured, interface design looks pretty decent. However, your code is lacking much of the reasonable level of function and class level Javadoc comments and the GUI design is non-responsive - either to screen rotation or resizing (the clock image which tends to cover other view controls, and camera/gallery controls disappear in landscape mode). I like the fact you allow your users switch between bright and dark mode too.

You implemented several of the required behaviours but the quality of some of the implementation is poor (e.g. tracking user's location during trip, instead of just the location where a picture is taken).

## Some typical bugs are below:

1. There are frequent reminders of "System UI is not responding" when users operate some actions incorrectly. You should improve your exception handling strategy - for example you are nesting try-catch structure, and have empty catch blocks, which does not offer a gracious handling of errors to users. 

2. When click Gallery to inspect the trip detail, it cannot return to the main UI interface. Only after "Deleting" it, it will return to the main UI. 

Lastly, merely printing stack or maining a log of exception is useful to developers, but not to users.

Also your code comment practice is pretty weak.