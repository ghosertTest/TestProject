// developerWorks e-mail to a friend JavaScript
// Adapted from Danny Goodman's
// "JavaScript Bible, 3rd Edition" (www.dannyg.com)
// Initialize global var for new window object
// so it can be accessed by all functions on the page
var forumWind
// make the new window and put some stuff in it
function forumWindow() {
        forumWind = window.open(forumURL,"forumsubwindow","HEIGHT=500,WIDTH=765,resizable=yes,scrollbars")
}        