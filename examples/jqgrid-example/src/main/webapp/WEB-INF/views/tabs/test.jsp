<input type="button" id="myPinButton" value="AAAA">


<script>

var state = myLayout.state;

//current state
var isWestPaneOpen       = !state.west.isClosed;
var isSouthPaneHidden    = state.south.isHidden;
var isEastPaneSliding    = state.east.isSliding;

// current dimensions
var westCurrentSize      = state.west.size;
var westMinimumSize      = state.west.minSize;
var containerInnerWidth  = state.container.innerWidth;
var containerInnerHeight  = state.container.innerHeight;
var containerPaddingLeft = state.container.paddingLeft;

//read layout 'options'
var is_west_resizable = myLayout.options.west.resizable;
var north_maxHeight   = myLayout.options.north.maxSize;

// get layout 'state'
var is_west_open = myLayout.state.west.isOpen;
var north_size   = myLayout.state.north.size;

var centerPane = myLayout.panes.north.size;

// layout methods
myLayout.toggle("north");
myLayout.sizePane("west", 300);

// layout utilities
myLayout.addPinBtn("#myPinButton", "west");
myLayout.allowOverflow("north");


/* outerLayout = $("body").layout({
    center__onresize: "innerLayout.resizeAll"
});
innerLayout = $("div.ui-layout-center").layout(); */

//alert($('.ui-tabs-nav').height());



</script>