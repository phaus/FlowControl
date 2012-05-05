/**
 * flowcontrol
 * 01.05.2012
 * @author Philipp Haussleiter
 *
 */

jQuery(function($) {
    // this is a fix for clearing non-JS Browsers captions.
    function setup(){
        $('.dropdown-toggle').dropdown();
        $('a i').select().html("");
        $('button i').select().html("");
        enhanceTable('notice');
        enhanceTable('project');
        enhanceTable('account');
        enhanceTable('notifier');
        enhanceTable('error');
    }

    function enhanceTable(table_class){
        $('table.'+table_class+' tr').each(function(index) {
            if($(this).find("td").length){
                var a = $(this).find('i.icon-eye-open').parent();
                $(this).css("cursor","pointer");
                $(this).click(function () {
                    window.location.href = a.attr("href");
                })
                a.remove();
            }
        });
    }
    setup();
    // make code pretty
    prettyPrint();

});