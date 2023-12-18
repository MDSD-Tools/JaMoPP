import '../css/project.css'
import $ from 'jquery';
import {CalendarReleases} from './calendar-releases'


$(document).ready(function () {

    var tabs = $('#content-project .tab-pane');
    var lis = $('#nav-project li');

    $('#nav-project a').click(function (e) {
        e.preventDefault();
        tabs.removeClass('active');
        lis.removeClass('active');
        $(this).parent().addClass('active');
        $($(this).attr('href')).addClass('active');
        try {
            if (history) {
                history.pushState(null, null, $(this).attr('href'));
            }
        } catch (e) {
        }
        if ($(this).attr('href') === '#support') {
            // Single project
            const tableReleases = document.querySelectorAll('.calendar-releases')
            tableReleases.forEach(calendar => {
                CalendarReleases.singleRelease(calendar);
            })
        } else {
            // Clean
            $('.timeline').detach()
        }
    });

    try {
        if (window && window.location && window.location.hash) {
            var f = null;
            lis.each(function () {
                if ($(this).find('a').attr('href') === window.location.hash) {
                    f = $(this);
                }
            });
            if (f) {
                lis.removeClass('active');
                tabs.removeClass('active');
                f.addClass('active');
                $(f.find('a').attr('href')).addClass('active');
            }
            if (f.find('a').attr('href') === '#support') {
                const tableReleases = document.querySelectorAll('.calendar-releases')
                tableReleases.forEach(calendar => {
                    CalendarReleases.singleRelease(calendar);
                })
            }
        }
    } catch (e) {
    }

    if ($('ul#proj-filters').length > 0) {
        var projects = $('a.project');
        $('ul#proj-filters li').click(function (e) {
            if ($(this).hasClass('active')) {
                projects.addClass('hide');
                var search = $(this).attr('data-group');
                projects.each(function () {
                    var data = $(this).attr('data-groups');
                    if (data.indexOf(search + ',') > -1 || data.indexOf(search + ']') > -1) {
                        $(this).removeClass('hide')
                    }
                })
            } else {
                projects.removeClass('hide');
            }
        });
    }

});
