/*! device.js 0.1.55 */
/*(function(){var a,b,c,d,e,f,g,h,i,j=[].indexOf||function(a){for(var b=0,c=this.length;c>b;b++)if(b in this&&this[b]===a)return b;return-1};window.device={},b=window.document.documentElement,i=window.navigator.userAgent.toLowerCase(),device.ios=function(){return device.iphone()||device.ipod()||device.ipad()},device.iphone=function(){return c("iphone")},device.ipod=function(){return c("ipod")},device.ipad=function(){return c("ipad")},device.android=function(){return c("android")},device.androidPhone=function(){return device.android()&&c("mobile")},device.androidTablet=function(){return device.android()&&!c("mobile")},device.blackberry=function(){return c("blackberry")||c("bb10")||c("rim")},device.blackberryPhone=function(){return device.blackberry()&&!c("tablet")},device.blackberryTablet=function(){return device.blackberry()&&c("tablet")},device.windows=function(){return c("windows")},device.windowsPhone=function(){return device.windows()&&c("phone")},device.windowsTablet=function(){return device.windows()&&c("touch")},device.mobile=function(){return device.androidPhone()||device.iphone()||device.ipod()||device.windowsPhone()||device.blackberryPhone()},device.tablet=function(){return device.ipad()||device.androidTablet()||device.blackberryTablet()||device.windowsTablet()},device.portrait=function(){return 90!==Math.abs(window.orientation)},device.landscape=function(){return 90===Math.abs(window.orientation)},c=function(a){return-1!==i.indexOf(a)},e=function(a){var c;return c=new RegExp(a,"i"),b.className.match(c)},a=function(a){return e(a)?void 0:b.className+=" "+a},g=function(a){return e(a)?b.className=b.className.replace(a,""):void 0},device.ios()?device.ipad()?a("ios ipad tablet"):device.iphone()?a("ios iphone mobile"):device.ipod()&&a("ios ipod mobile"):device.android()?device.androidTablet()?a("android tablet"):a("android mobile"):device.blackberry()?device.blackberryTablet()?a("blackberry tablet"):a("blackberry mobile"):device.windows()?device.windowsTablet()?a("windows tablet"):device.windowsPhone()?a("windows mobile"):a("desktop"):a("desktop"),d=function(){return device.landscape()?(g("portrait"),a("landscape")):(g("landscape"),a("portrait"))},h=j.call(window,"onorientationchange")>=0,f=h?"orientationchange":"resize",window.addEventListener?window.addEventListener(f,d,!1):window.attachEvent?window.attachEvent(f,d):window[f]=d,d()}).call(this);*/

/*!
* jQuery Transit - CSS3 transitions and transformations
* (c) 2011-2014 Rico Sta. Cruz
* MIT Licensed.
*
* http://ricostacruz.com/jquery.transit
* http://github.com/rstacruz/jquery.transit
*/;(function(root,factory){if(typeof define==='function'&&define.amd){define(['jquery'],factory);}else if(typeof exports==='object'){module.exports=factory(require('jquery'));}else{factory(root.jQuery);}}(this,function($){$.transit={version:"0.9.12",propertyMap:{marginLeft:'margin',marginRight:'margin',marginBottom:'margin',marginTop:'margin',paddingLeft:'padding',paddingRight:'padding',paddingBottom:'padding',paddingTop:'padding'},enabled:true,useTransitionEnd:false};var div=document.createElement('div');var support={};function getVendorPropertyName(prop){if(prop in div.style)return prop;var prefixes=['Moz','Webkit','O','ms'];var prop_=prop.charAt(0).toUpperCase()+prop.substr(1);for(var i=0;i<prefixes.length;++i){var vendorProp=prefixes[i]+prop_;if(vendorProp in div.style){return vendorProp;}}}
function checkTransform3dSupport(){div.style[support.transform]='';div.style[support.transform]='rotateY(90deg)';return div.style[support.transform]!=='';}
var isChrome=navigator.userAgent.toLowerCase().indexOf('chrome')>-1;support.transition=getVendorPropertyName('transition');support.transitionDelay=getVendorPropertyName('transitionDelay');support.transform=getVendorPropertyName('transform');support.transformOrigin=getVendorPropertyName('transformOrigin');support.filter=getVendorPropertyName('Filter');support.transform3d=checkTransform3dSupport();var eventNames={'transition':'transitionend','MozTransition':'transitionend','OTransition':'oTransitionEnd','WebkitTransition':'webkitTransitionEnd','msTransition':'MSTransitionEnd'};var transitionEnd=support.transitionEnd=eventNames[support.transition]||null;for(var key in support){if(support.hasOwnProperty(key)&&typeof $.support[key]==='undefined'){$.support[key]=support[key];}}
div=null;$.cssEase={'_default':'ease','in':'ease-in','out':'ease-out','in-out':'ease-in-out','snap':'cubic-bezier(0,1,.5,1)','easeInCubic':'cubic-bezier(.550,.055,.675,.190)','easeOutCubic':'cubic-bezier(.215,.61,.355,1)','easeInOutCubic':'cubic-bezier(.645,.045,.355,1)','easeInCirc':'cubic-bezier(.6,.04,.98,.335)','easeOutCirc':'cubic-bezier(.075,.82,.165,1)','easeInOutCirc':'cubic-bezier(.785,.135,.15,.86)','easeInExpo':'cubic-bezier(.95,.05,.795,.035)','easeOutExpo':'cubic-bezier(.19,1,.22,1)','easeInOutExpo':'cubic-bezier(1,0,0,1)','easeInQuad':'cubic-bezier(.55,.085,.68,.53)','easeOutQuad':'cubic-bezier(.25,.46,.45,.94)','easeInOutQuad':'cubic-bezier(.455,.03,.515,.955)','easeInQuart':'cubic-bezier(.895,.03,.685,.22)','easeOutQuart':'cubic-bezier(.165,.84,.44,1)','easeInOutQuart':'cubic-bezier(.77,0,.175,1)','easeInQuint':'cubic-bezier(.755,.05,.855,.06)','easeOutQuint':'cubic-bezier(.23,1,.32,1)','easeInOutQuint':'cubic-bezier(.86,0,.07,1)','easeInSine':'cubic-bezier(.47,0,.745,.715)','easeOutSine':'cubic-bezier(.39,.575,.565,1)','easeInOutSine':'cubic-bezier(.445,.05,.55,.95)','easeInBack':'cubic-bezier(.6,-.28,.735,.045)','easeOutBack':'cubic-bezier(.175, .885,.32,1.275)','easeInOutBack':'cubic-bezier(.68,-.55,.265,1.55)'};$.cssHooks['transit:transform']={get:function(elem){return $(elem).data('transform')||new Transform();},set:function(elem,v){var value=v;if(!(value instanceof Transform)){value=new Transform(value);}
if(support.transform==='WebkitTransform'&&!isChrome){elem.style[support.transform]=value.toString(true);}else{elem.style[support.transform]=value.toString();}
$(elem).data('transform',value);}};$.cssHooks.transform={set:$.cssHooks['transit:transform'].set};$.cssHooks.filter={get:function(elem){return elem.style[support.filter];},set:function(elem,value){elem.style[support.filter]=value;}};if($.fn.jquery<"1.8"){$.cssHooks.transformOrigin={get:function(elem){return elem.style[support.transformOrigin];},set:function(elem,value){elem.style[support.transformOrigin]=value;}};$.cssHooks.transition={get:function(elem){return elem.style[support.transition];},set:function(elem,value){elem.style[support.transition]=value;}};}
registerCssHook('scale');registerCssHook('scaleX');registerCssHook('scaleY');registerCssHook('translate');registerCssHook('rotate');registerCssHook('rotateX');registerCssHook('rotateY');registerCssHook('rotate3d');registerCssHook('perspective');registerCssHook('skewX');registerCssHook('skewY');registerCssHook('x',true);registerCssHook('y',true);function Transform(str){if(typeof str==='string'){this.parse(str);}
return this;}
Transform.prototype={setFromString:function(prop,val){var args=(typeof val==='string')?val.split(','):(val.constructor===Array)?val:[val];args.unshift(prop);Transform.prototype.set.apply(this,args);},set:function(prop){var args=Array.prototype.slice.apply(arguments,[1]);if(this.setter[prop]){this.setter[prop].apply(this,args);}else{this[prop]=args.join(',');}},get:function(prop){if(this.getter[prop]){return this.getter[prop].apply(this);}else{return this[prop]||0;}},setter:{rotate:function(theta){this.rotate=unit(theta,'deg');},rotateX:function(theta){this.rotateX=unit(theta,'deg');},rotateY:function(theta){this.rotateY=unit(theta,'deg');},scale:function(x,y){if(y===undefined){y=x;}
this.scale=x+","+y;},skewX:function(x){this.skewX=unit(x,'deg');},skewY:function(y){this.skewY=unit(y,'deg');},perspective:function(dist){this.perspective=unit(dist,'px');},x:function(x){this.set('translate',x,null);},y:function(y){this.set('translate',null,y);},translate:function(x,y){if(this._translateX===undefined){this._translateX=0;}
if(this._translateY===undefined){this._translateY=0;}
if(x!==null&&x!==undefined){this._translateX=unit(x,'px');}
if(y!==null&&y!==undefined){this._translateY=unit(y,'px');}
this.translate=this._translateX+","+this._translateY;}},getter:{x:function(){return this._translateX||0;},y:function(){return this._translateY||0;},scale:function(){var s=(this.scale||"1,1").split(',');if(s[0]){s[0]=parseFloat(s[0]);}
if(s[1]){s[1]=parseFloat(s[1]);}
return(s[0]===s[1])?s[0]:s;},rotate3d:function(){var s=(this.rotate3d||"0,0,0,0deg").split(',');for(var i=0;i<=3;++i){if(s[i]){s[i]=parseFloat(s[i]);}}
if(s[3]){s[3]=unit(s[3],'deg');}
return s;}},parse:function(str){var self=this;str.replace(/([a-zA-Z0-9]+)\((.*?)\)/g,function(x,prop,val){self.setFromString(prop,val);});},toString:function(use3d){var re=[];for(var i in this){if(this.hasOwnProperty(i)){if((!support.transform3d)&&((i==='rotateX')||(i==='rotateY')||(i==='perspective')||(i==='transformOrigin'))){continue;}
if(i[0]!=='_'){if(use3d&&(i==='scale')){re.push(i+"3d("+this[i]+",1)");}else if(use3d&&(i==='translate')){re.push(i+"3d("+this[i]+",0)");}else{re.push(i+"("+this[i]+")");}}}}
return re.join(" ");}};function callOrQueue(self,queue,fn){if(queue===true){self.queue(fn);}else if(queue){self.queue(queue,fn);}else{self.each(function(){fn.call(this);});}}
function getProperties(props){var re=[];$.each(props,function(key){key=$.camelCase(key);key=$.transit.propertyMap[key]||$.cssProps[key]||key;key=uncamel(key);if(support[key])
key=uncamel(support[key]);if($.inArray(key,re)===-1){re.push(key);}});return re;}
function getTransition(properties,duration,easing,delay){var props=getProperties(properties);if($.cssEase[easing]){easing=$.cssEase[easing];}
var attribs=''+toMS(duration)+' '+easing;if(parseInt(delay,10)>0){attribs+=' '+toMS(delay);}
var transitions=[];$.each(props,function(i,name){transitions.push(name+' '+attribs);});return transitions.join(', ');}
$.fn.transition=$.fn.transit=function(properties,duration,easing,callback){var self=this;var delay=0;var queue=true;var theseProperties=$.extend(true,{},properties);if(typeof duration==='function'){callback=duration;duration=undefined;}
if(typeof duration==='object'){easing=duration.easing;delay=duration.delay||0;queue=typeof duration.queue==="undefined"?true:duration.queue;callback=duration.complete;duration=duration.duration;}
if(typeof easing==='function'){callback=easing;easing=undefined;}
if(typeof theseProperties.easing!=='undefined'){easing=theseProperties.easing;delete theseProperties.easing;}
if(typeof theseProperties.duration!=='undefined'){duration=theseProperties.duration;delete theseProperties.duration;}
if(typeof theseProperties.complete!=='undefined'){callback=theseProperties.complete;delete theseProperties.complete;}
if(typeof theseProperties.queue!=='undefined'){queue=theseProperties.queue;delete theseProperties.queue;}
if(typeof theseProperties.delay!=='undefined'){delay=theseProperties.delay;delete theseProperties.delay;}
if(typeof duration==='undefined'){duration=$.fx.speeds._default;}
if(typeof easing==='undefined'){easing=$.cssEase._default;}
duration=toMS(duration);var transitionValue=getTransition(theseProperties,duration,easing,delay);var work=$.transit.enabled&&support.transition;var i=work?(parseInt(duration,10)+parseInt(delay,10)):0;if(i===0){var fn=function(next){self.css(theseProperties);if(callback){callback.apply(self);}
if(next){next();}};callOrQueue(self,queue,fn);return self;}
var oldTransitions={};var run=function(nextCall){var bound=false;var cb=function(){if(bound){self.unbind(transitionEnd,cb);}
if(i>0){self.each(function(){this.style[support.transition]=(oldTransitions[this]||null);});}
if(typeof callback==='function'){callback.apply(self);}
if(typeof nextCall==='function'){nextCall();}};if((i>0)&&(transitionEnd)&&($.transit.useTransitionEnd)){bound=true;self.bind(transitionEnd,cb);}else{window.setTimeout(cb,i);}
self.each(function(){if(i>0){this.style[support.transition]=transitionValue;}
$(this).css(theseProperties);});};var deferredRun=function(next){this.offsetWidth;run(next);};callOrQueue(self,queue,deferredRun);return this;};function registerCssHook(prop,isPixels){if(!isPixels){$.cssNumber[prop]=true;}
$.transit.propertyMap[prop]=support.transform;$.cssHooks[prop]={get:function(elem){var t=$(elem).css('transit:transform');return t.get(prop);},set:function(elem,value){var t=$(elem).css('transit:transform');t.setFromString(prop,value);$(elem).css({'transit:transform':t});}};}
function uncamel(str){return str.replace(/([A-Z])/g,function(letter){return '-'+letter.toLowerCase();});}
function unit(i,units){if((typeof i==="string")&&(!i.match(/^[\-0-9\.]+$/))){return i;}else{return ""+i+units;}}
function toMS(duration){var i=duration;if(typeof i==='string'&&(!i.match(/^[\-0-9\.]+/))){i=$.fx.speeds[i]||$.fx.speeds._default;}
return unit(i,'ms');}
$.transit.getTransitionValue=getTransition;return $;}));

// jQuery Mobile Swipe Event
(function(e,t,n){typeof define=="function"&&define.amd?define(["jquery"],function(r){return n(r,e,t),r.mobile}):n(e.jQuery,e,t)})(this,document,function(e,t,n,r){(function(e,t,n,r){function T(e){while(e&&typeof e.originalEvent!="undefined")e=e.originalEvent;return e}function N(t,n){var i=t.type,s,o,a,l,c,h,p,d,v;t=e.Event(t),t.type=n,s=t.originalEvent,o=e.event.props,i.search(/^(mouse|click)/)>-1&&(o=f);if(s)for(p=o.length,l;p;)l=o[--p],t[l]=s[l];i.search(/mouse(down|up)|click/)>-1&&!t.which&&(t.which=1);if(i.search(/^touch/)!==-1){a=T(s),i=a.touches,c=a.changedTouches,h=i&&i.length?i[0]:c&&c.length?c[0]:r;if(h)for(d=0,v=u.length;d<v;d++)l=u[d],t[l]=h[l]}return t}function C(t){var n={},r,s;while(t){r=e.data(t,i);for(s in r)r[s]&&(n[s]=n.hasVirtualBinding=!0);t=t.parentNode}return n}function k(t,n){var r;while(t){r=e.data(t,i);if(r&&(!n||r[n]))return t;t=t.parentNode}return null}function L(){g=!1}function A(){g=!0}function O(){E=0,v.length=0,m=!1,A()}function M(){L()}function _(){D(),c=setTimeout(function(){c=0,O()},e.vmouse.resetTimerDuration)}function D(){c&&(clearTimeout(c),c=0)}function P(t,n,r){var i;if(r&&r[t]||!r&&k(n.target,t))i=N(n,t),e(n.target).trigger(i);return i}function H(t){var n=e.data(t.target,s),r;!m&&(!E||E!==n)&&(r=P("v"+t.type,t),r&&(r.isDefaultPrevented()&&t.preventDefault(),r.isPropagationStopped()&&t.stopPropagation(),r.isImmediatePropagationStopped()&&t.stopImmediatePropagation()))}function B(t){var n=T(t).touches,r,i,o;n&&n.length===1&&(r=t.target,i=C(r),i.hasVirtualBinding&&(E=w++,e.data(r,s,E),D(),M(),d=!1,o=T(t).touches[0],h=o.pageX,p=o.pageY,P("vmouseover",t,i),P("vmousedown",t,i)))}function j(e){if(g)return;d||P("vmousecancel",e,C(e.target)),d=!0,_()}function F(t){if(g)return;var n=T(t).touches[0],r=d,i=e.vmouse.moveDistanceThreshold,s=C(t.target);d=d||Math.abs(n.pageX-h)>i||Math.abs(n.pageY-p)>i,d&&!r&&P("vmousecancel",t,s),P("vmousemove",t,s),_()}function I(e){if(g)return;A();var t=C(e.target),n,r;P("vmouseup",e,t),d||(n=P("vclick",e,t),n&&n.isDefaultPrevented()&&(r=T(e).changedTouches[0],v.push({touchID:E,x:r.clientX,y:r.clientY}),m=!0)),P("vmouseout",e,t),d=!1,_()}function q(t){var n=e.data(t,i),r;if(n)for(r in n)if(n[r])return!0;return!1}function R(){}function U(t){var n=t.substr(1);return{setup:function(){q(this)||e.data(this,i,{});var r=e.data(this,i);r[t]=!0,l[t]=(l[t]||0)+1,l[t]===1&&b.bind(n,H),e(this).bind(n,R),y&&(l.touchstart=(l.touchstart||0)+1,l.touchstart===1&&b.bind("touchstart",B).bind("touchend",I).bind("touchmove",F).bind("scroll",j))},teardown:function(){--l[t],l[t]||b.unbind(n,H),y&&(--l.touchstart,l.touchstart||b.unbind("touchstart",B).unbind("touchmove",F).unbind("touchend",I).unbind("scroll",j));var r=e(this),s=e.data(this,i);s&&(s[t]=!1),r.unbind(n,R),q(this)||r.removeData(i)}}}var i="virtualMouseBindings",s="virtualTouchID",o="vmouseover vmousedown vmousemove vmouseup vclick vmouseout vmousecancel".split(" "),u="clientX clientY pageX pageY screenX screenY".split(" "),a=e.event.mouseHooks?e.event.mouseHooks.props:[],f=e.event.props.concat(a),l={},c=0,h=0,p=0,d=!1,v=[],m=!1,g=!1,y="addEventListener"in n,b=e(n),w=1,E=0,S,x;e.vmouse={moveDistanceThreshold:10,clickDistanceThreshold:10,resetTimerDuration:1500};for(x=0;x<o.length;x++)e.event.special[o[x]]=U(o[x]);y&&n.addEventListener("click",function(t){var n=v.length,r=t.target,i,o,u,a,f,l;if(n){i=t.clientX,o=t.clientY,S=e.vmouse.clickDistanceThreshold,u=r;while(u){for(a=0;a<n;a++){f=v[a],l=0;if(u===r&&Math.abs(f.x-i)<S&&Math.abs(f.y-o)<S||e.data(u,s)===f.touchID){t.preventDefault(),t.stopPropagation();return}}u=u.parentNode}}},!0)})(e,t,n),function(e){e.mobile={}}(e),function(e,t){var r={touch:"ontouchend"in n};e.mobile.support=e.mobile.support||{},e.extend(e.support,r),e.extend(e.mobile.support,r)}(e),function(e,t,r){function l(t,n,i,s){var o=i.type;i.type=n,s?e.event.trigger(i,r,t):e.event.dispatch.call(t,i),i.type=o}var i=e(n),s=e.mobile.support.touch,o="touchmove scroll",u=s?"touchstart":"mousedown",a=s?"touchend":"mouseup",f=s?"touchmove":"mousemove";e.each("touchstart touchmove touchend tap taphold swipe swipeleft swiperight scrollstart scrollstop".split(" "),function(t,n){e.fn[n]=function(e){return e?this.bind(n,e):this.trigger(n)},e.attrFn&&(e.attrFn[n]=!0)}),e.event.special.scrollstart={enabled:!0,setup:function(){function s(e,n){r=n,l(t,r?"scrollstart":"scrollstop",e)}var t=this,n=e(t),r,i;n.bind(o,function(t){if(!e.event.special.scrollstart.enabled)return;r||s(t,!0),clearTimeout(i),i=setTimeout(function(){s(t,!1)},50)})},teardown:function(){e(this).unbind(o)}},e.event.special.tap={tapholdThreshold:750,emitTapOnTaphold:!0,setup:function(){var t=this,n=e(t),r=!1;n.bind("vmousedown",function(s){function a(){clearTimeout(u)}function f(){a(),n.unbind("vclick",c).unbind("vmouseup",a),i.unbind("vmousecancel",f)}function c(e){f(),!r&&o===e.target?l(t,"tap",e):r&&e.preventDefault()}r=!1;if(s.which&&s.which!==1)return!1;var o=s.target,u;n.bind("vmouseup",a).bind("vclick",c),i.bind("vmousecancel",f),u=setTimeout(function(){e.event.special.tap.emitTapOnTaphold||(r=!0),l(t,"taphold",e.Event("taphold",{target:o}))},e.event.special.tap.tapholdThreshold)})},teardown:function(){e(this).unbind("vmousedown").unbind("vclick").unbind("vmouseup"),i.unbind("vmousecancel")}},e.event.special.swipe={scrollSupressionThreshold:30,durationThreshold:1e3,horizontalDistanceThreshold:30,verticalDistanceThreshold:30,getLocation:function(e){var n=t.pageXOffset,r=t.pageYOffset,i=e.clientX,s=e.clientY;if(e.pageY===0&&Math.floor(s)>Math.floor(e.pageY)||e.pageX===0&&Math.floor(i)>Math.floor(e.pageX))i-=n,s-=r;else if(s<e.pageY-r||i<e.pageX-n)i=e.pageX-n,s=e.pageY-r;return{x:i,y:s}},start:function(t){var n=t.originalEvent.touches?t.originalEvent.touches[0]:t,r=e.event.special.swipe.getLocation(n);return{time:(new Date).getTime(),coords:[r.x,r.y],origin:e(t.target)}},stop:function(t){var n=t.originalEvent.touches?t.originalEvent.touches[0]:t,r=e.event.special.swipe.getLocation(n);return{time:(new Date).getTime(),coords:[r.x,r.y]}},handleSwipe:function(t,n,r,i){if(n.time-t.time<e.event.special.swipe.durationThreshold&&Math.abs(t.coords[0]-n.coords[0])>e.event.special.swipe.horizontalDistanceThreshold&&Math.abs(t.coords[1]-n.coords[1])<e.event.special.swipe.verticalDistanceThreshold){var s=t.coords[0]>n.coords[0]?"swipeleft":"swiperight";return l(r,"swipe",e.Event("swipe",{target:i,swipestart:t,swipestop:n}),!0),l(r,s,e.Event(s,{target:i,swipestart:t,swipestop:n}),!0),!0}return!1},eventInProgress:!1,setup:function(){var t,n=this,r=e(n),s={};t=e.data(this,"mobile-events"),t||(t={length:0},e.data(this,"mobile-events",t)),t.length++,t.swipe=s,s.start=function(t){if(e.event.special.swipe.eventInProgress)return;e.event.special.swipe.eventInProgress=!0;var r,o=e.event.special.swipe.start(t),u=t.target,l=!1;s.move=function(t){if(!o||t.isDefaultPrevented())return;r=e.event.special.swipe.stop(t),l||(l=e.event.special.swipe.handleSwipe(o,r,n,u),l&&(e.event.special.swipe.eventInProgress=!1)),Math.abs(o.coords[0]-r.coords[0])>e.event.special.swipe.scrollSupressionThreshold&&t.preventDefault()},s.stop=function(){l=!0,e.event.special.swipe.eventInProgress=!1,i.off(f,s.move),s.move=null},i.on(f,s.move).one(a,s.stop)},r.on(u,s.start)},teardown:function(){var t,n;t=e.data(this,"mobile-events"),t&&(n=t.swipe,delete t.swipe,t.length--,t.length===0&&e.removeData(this,"mobile-events")),n&&(n.start&&e(this).off(u,n.start),n.move&&i.off(f,n.move),n.stop&&i.off(a,n.stop))}},e.each({scrollstop:"scrollstart",taphold:"tap",swipeleft:"swipe.left",swiperight:"swipe.right"},function(t,n){e.event.special[t]={setup:function(){e(this).bind(n,e.noop)},teardown:function(){e(this).unbind(n)}}})}(e,this)});





/* common add prototype */
// custom method
if (typeof Function.prototype.method !== "funciton") {
    Function.prototype.method = function (name, implementation) {
        this.prototype[name] = implementation;
        return this;
    };
};


if (typeof Function.prototype.bind === "undefined") {
    Function.prototype.bind = function(thisArg) {
        var fn = this,
            slice = Array.prototype.slice,
            args = slice.call(arguments, 1);
        
        return function () {
            return fn.apply(thisArg, args.concat(slice.call(arguments)))
        }
    }
}

if (!String.prototype.trim) {
    String.prototype.trim = function () {
        return String(this).replace(/^\s+/, '').replace(/\s+$/, '');
    };
}


;(function(global, undefined){
    "use strict";

    var _configs = typeof myappConfigs === 'undefined' ? {} : myappConfigs;
    window.LIB_NAME = _configs.name || 'KM2';
    window.IS_DEBUG = _configs.debug || location.href.indexOf('debug=true') >= 0;

    var LIB_NAME = global.LIB_NAME || 'KM2';
    if (global[LIB_NAME]) {
        return;
    }

    var core = global[LIB_NAME] || (global[LIB_NAME] = {}),
        arrayProto = Array.prototype,
        objectProto = Object.prototype,
        toString = objectProto.toString,
        hasOwn = objectProto.hasOwnProperty,
        arraySlice = arrayProto.slice,
        isArray = Array.isArray || function (a) { return toString.call(a) === '[object Array]'; },

        name_space = function (ns_string){
            var parts = ns_string.split('.'),
                parent = KM2,
                i;
            if (parts[0] === "KM2") {
                parts = parts.slice(1);
            };
            for (i = 0; i < parts.length -1; i+=1) {
                if ( typeof parent[parts[i]] == "undefined" ) {
                    parent[parts[i]] = {};
                };
                parent = parent[parts[i]] = {};
            };
            return parent;
        },
        each = function (obj, iterater, ctx) {
            if (!obj) {
                return obj;
            };
            var i = 0,
                len = 0,
                isArr = isArray(obj);

            if (isArr) {
                // 배열
                for (i = 0, len = obj.length; i < len; i++) {
                    if (iterater.call(ctx || obj, obj[i], i, obj) === false) {
                        break;
                    }
                };
            } else {
                // 객체
                for (i in obj) {
                    if (hasOwn.call(obj, i)) {
                        if (iterater.call(ctx || obj, obj[i], i, obj) === false) {
                            break;
                        };
                    };
                };
            }
            return obj;
        },
        extend = function (deep, obj) {
            var args;
            if (deep === true) {
                args = arraySlice.call(arguments, 2);
            } else {
                args = arraySlice.call(arguments, 1);
                obj = deep;
                deep = false;
            }
            each(args, function (source) {
                if (!source) {
                    return;
                }

                each(source, function (val, key) {
                    var isArr = isArray(val);
                    if (deep && (isArr || isPlainObject(val))) {
                        obj[key] || (obj[key] = isArr ? [] : {});
                        obj[key] = extend(deep, obj[key], val);
                    } else {
                        obj[key] = val;
                    }
                });
            });
            return obj;
        },
        isType = function (value, typeName) {
            var isGet = arguments.length === 1;

            function result(name) {
                return isGet ? name : typeName === name;
            }

            if (value === null) {
                return result('null');
            }

            if (typeof value === undefined) {
                return 'undefined'
            }

            if (value && value.nodeType) {
                if (value.nodeType === 1 || value.nodeType === 9) {
                    return result('element');
                } else if (value && value.nodeType === 3 && value.nodeName === '#text') {
                    return result('textnode');
                }
            }

            if (typeName === 'object' || typeName === 'json') {
                return isGet ? 'object' : isPlainObject(value);
            }

            var s = toString.call(value),
                type = s.match(/\[object (.*?)\]/)[1].toLowerCase();

            if (type === 'number') {
                if (isNaN(value)) {
                    return result('nan');
                }
                if (!isFinite(value)) {
                    return result('infinity');
                }
                return result('number');
            }

            return isGet ? type : type === typeName;
        },
        ui = function (name, /* supr, */ attr) {

        };
    extend(core, {

        name : LIB_NAME,

        name_space : name_space,

        each : each,

        isType : isType,

        extend : extend,

        ui : ui

    });

})(window);

;(function(global, core, $, undefined){
    'use strict';

    var Header = function (options) {
        this.$element = $(this)
        this.options = options;
        
    };

    /**
     * 애니메이션
     */
    Header.prototype.DURATION = 400; 

    /**
     * 이벤트
     * @param e Event Object 
     */
    Header.prototype.bindEvent = function(e){
        var $this = $(this),
            clickType = $this.data('selector');
        if(core.isType(clickType) === "string") {
            Header.prototype[clickType](clickType);
        }else{
            return false;
        }
        e.preventDefault();
        e.stopPropagation();
    }

    /**
     * 전체메뉴
     * @param type [data-selector] 값
     */
    Header.prototype.allMenu = function(type){
        this.sideLayerAction(type)
    };

    /**
     * 마이페이지
     * @param type [data-selector] 값
     */
    Header.prototype.studentList = function(type){
        this.sideLayerAction(type)
    };

    /**
     * 검색
     */
    Header.prototype.search = function(){
        var elem = $('[data-selector=search]');
        var target = '.search-slideout';
        var layerOpen = this.checkOpenMenu();

        if ( layerOpen !== null && layerOpen !== elem.data('selector')){
            $('[data-selector='+layerOpen+']').removeClass('on')
            elem.addClass('on')
            this.replaceLayer(target);
            return false;
        }
        
        if ( elem.hasClass('queue') ) return false;
        if (elem.hasClass('on')){
            elem.removeClass('on');
            elem.addClass('queue');
            this.hideLAyer(elem, target)
        }else{
            elem.addClass('on');
            elem.addClass('queue');
            this.showLayer(elem, target)
        };
        $(target).on('click', '.slideout-btn', function(e){
            if ( elem.hasClass('queue') ) return false;
            Header.prototype.search();
            $(target).off('click');
        })
    };

    /**
     * 마이 메뉴
     */
    Header.prototype.myMenu = function(){
        var elem = $('[data-selector=myMenu]');
        var target = '.mymenu-slideout';
        var layerOpen = this.checkOpenMenu();

        if ( layerOpen !== null && layerOpen !== elem.data('selector')){
            $('[data-selector='+layerOpen+']').removeClass('on')
            elem.addClass('on')
            this.replaceLayer(target);
            return false;
        }
        if ( elem.hasClass('queue') ) return false;
        if (elem.hasClass('on')){
            elem.removeClass('on');
            elem.addClass('queue');
            this.hideLAyer(elem, target)
        }else{
            elem.addClass('on');
            elem.addClass('queue');
            this.showLayer(elem, target)
        }
        $(target).on('click', '.slideout-btn', function(e){
            if ( elem.hasClass('queue') ) return false;
            Header.prototype.myMenu();
            $(target).off('click');
        })
    };

    /**
     * 마이메뉴, 검색 중 오픈되어 있는 메뉴 검색
     */
    Header.prototype.checkOpenMenu = function(){
        var elems = $('.mHeader').find('[data-selector]');
        var selector = null;
        elems.each(function(e){
            var $this = $(this);
            if ($this.hasClass('on')){
                selector = $this.data('selector');
            };
        });
        return selector;
    };

    /**
     * 마이메뉴, 검색 메뉴 오픈
     * @param that 메뉴 버튼 Element
     * @param elem 메뉴 컨텐츠 클래스 네임.
     */
    Header.prototype.showLayer = function(that, elem){
        var wrap = $('.mHeader-sub-wrap');
        var target = wrap.find(elem);
        var that = that;
        if(wrap.hasClass('active')){
            wrap.find('>div').hide();
        }else{
            wrap.addClass('active');
            wrap.css({"height":0});
        };
        wrap.show();
        target.show();
        wrap.animate({"height":target.height()+"px"},  this.DURATION, function(){
            $(this).removeAttr('style');
            that.removeClass('queue');
        });   
    };

    /**
     * 마이메뉴, 검색 메뉴 닫기 버튼
     */
    Header.prototype.hideLAyer = function(that, elem){
        var wrap = $('.mHeader-sub-wrap');
        var target = wrap.find(elem);
        wrap.css({"height":target.height()+"px"});
        wrap.animate({"height":0}, this.DURATION, function(){
            $(this).removeAttr('style');
            $(this).removeClass('active');
            that.removeClass('queue');
            target.hide();
        });
    }

    /**
     * 마이메뉴, 검색 메뉴 중 열려 있는 메뉴가 있을 경우 클릭한 메뉴 컨텐츠로 대체
     * @param elem 오픈되어야 할 메뉴 컨텐츠 클래스 네임
     */
    Header.prototype.replaceLayer = function(elem){
        var wrap = $('.mHeader-sub-wrap');
        var that = this;
        var target = wrap.find(elem);
        wrap.find('>div').hide();
        target.show();
        $(target).find('.slideout-btn').on('click', function(e){
            if (elem === '.mymenu-slideout'){
                that.myMenu();
            }else{
                that.search();
            }
            $(target).find('.slideout-btn').off('click')
        })
    };

    /**
     * 전체 메뉴, 마이페이지 컨트롤 메서드
     * @param type 전체메뉴, 마이페이지 구분
     * @param close 닫기 버튼 클릭 여부
     */
    Header.prototype.sideLayerAction = function(type, close){
        var layerOpen = this.checkOpenMenu();
        var that = this;
        var type = type
        var topBar = $('.mHeader');
        var elem = (type === 'allMenu') ? $('.allmenu-slideout') : $('.studentList-slideout')
        var pos;
        if (layerOpen !== null) this[layerOpen]();
        if (close === 'close'){
            pos = '0';
            $('html').removeClass('side-open')
        }else{
            $('html').addClass('side-open')
            pos = (type === 'allMenu') ? '100%' : '-100%';
        }
        topBar.transition({ x: pos }, this.DURATION);
        elem.transition({ x: pos }, this.DURATION);
        elem.one('click', '.slideout-btn', function(e){
            that.sideLayerAction(type, 'close');
            e.preventDefault();
            e.stopPropagation();
            elem.off('click')
        })
    };

    /**
     * 제이쿼리 함수로 바인드
     */
    function Plugin(option){
        var $this = $(this);
        var data    = $this.data('Header')
        var options = $.extend({}, Header.DEFAULTS, $this.data(), typeof option == 'object' && option);
        var action  = typeof option == 'string' ? option : options.slide
        if (!data) $this.data('Header', (data = new Header(this, options)));
        if (action) data[action]()
    }

    $.fn.Header             = Plugin
    $.fn.Header.Constructor = Header
    // $(window).on('load', function () {
    //     Plugin.call($('.header'), $('.header').data())
    // });


    $(document).on('click', '[data-selector]',Header.prototype.bindEvent);
    // $(function(){
    //     $('.mHeader').Header('replaceLayer')
    // })
})(window, window[LIB_NAME], jQuery);





;(function(global, core, $, undefined){
    var km2_ui = core.km2_ui = {
		init : function(){
            this.modules();
            this.answerListSwipe();
		},
        modules : function(){
            /**
             * KM2_04_02_11.html only 체크 전용 
             */
            var checkCircle = $('[data-modules="checkCircle"]');
            checkCircle.on('click', function(e){
                e.preventDefault()
                var $this = $(this);
                var toggle = $this.data('toggle');
                var checked = $this.data('checkStatus');
                var ico = $(this).find('.check_circle');
                if(toggle){
                    if (checked){
                        $this.data('checkStatus', false);
                        $this.removeClass('comp')
                        ico.removeClass('complete');
                    }else{
                        
                        $this.data('checkStatus', true);
                        $this.addClass('comp')
                        ico.addClass('complete');
                    }
                }else{
                    if (checked) return false;
                    $this.data('checkStatus', true);
                    ico.addClass('complete');
                    $this.addClass('comp')
                };
            });

            /**
             * 화면 고정 스티키 컨텐츠.
             */
            var sticky = $('[data-module="sticky"]');
            if (sticky.length) {
                var pos = sticky.offset().top;
                var height = sticky.height();
                $(document).on('scroll', function(e){
                    var $this = $(this);
                    if($this.scrollTop() >= pos){
                        sticky.addClass('on');
                        sticky.css({"height":height+"px"});
                    }else{
                        sticky.removeClass('on');
                        sticky.removeAttr('style');
                    ;}
                });
            };

            /**
             * 좌우 스크롤 컨텐츠
             */
            var overScroll = $('[data-module="overscroll"]');
            if (overScroll.length) {
                
                $(window).on('load', function(){
                    
                    var size = 0;
                    overScroll.find('.scroll-inner > li').each(function(){
                        var $this = $(this);
                        var width = parseFloat(window.getComputedStyle($this[0],null).getPropertyValue("width"), 10);
                        size +=width;
                    });
                    // console.log(size)
                    overScroll.find('.scroll-inner').css('width',Math.ceil(size)+'px')
                    overScroll.mCustomScrollbar({
                        axis:"x"
                    });
                    overScroll.mCustomScrollbar("update");
                })
            };
        },
        
        /**
         * 해답지 좌, 우 스와이프
         * KM2.km2_ui.answerListSwipe()  함수 호출시 기능 동작
         */
        answerListSwipe : function(){
            var answerActive = $('[data-modules="answerList"]');
            
            answerActive.find('li').on('swipeleft', function(){
                var $this = $(this);
                swipeleftHandler($this);
            })
            answerActive.find('li').on('swiperight', function(){
                var $this = $(this);
                swiperightHandler($this);
            })

            function swipeleftHandler(elem){
                elem.children(".answer-check.td").removeClass('active');
                elem.children(".answer-check.td").find("input").removeAttr('disabled','disabled');
            }
            function swiperightHandler(elem){
                elem.children(".answer-check.td").addClass('active');
                elem.children(".answer-check.td").find("input").attr('disabled','disabled');
            }
        }

	}


})(window, window[LIB_NAME], jQuery);

//table row 
function tableToggle(){
	var target = $(this).attr('data-toggle');
	$(this).toggleClass('on');
	$(target).toggleClass('on').closest('.table').addClass('overflowhide');
	return false;
}

$(function(){
    KM2.km2_ui.init();
});

$(document).ready(function() {
    $(".booklet-check-group input:checked[type='radio']").addClass('active');   
    $(".booklet-check-group input[type='radio']").click(function() {
        $(this).prop('checked', false);
        $(this).toggleClass('active');

        if( $(this).hasClass('active') ) {
            $(this).prop('checked', true);
            $(document).find(".booklet-check-group input:not(:checked)[type='radio']").removeClass('active');
        }
    });
    
    var loaderCheck = $('#ajaxLoader').text();
	if(loaderCheck.length != 0){
		$(document).ajaxStart(function () {
			$('#topHtml').addClass("loading");
			$('#ajaxLoader').css("display","");
		});
		$( document ).ajaxStop(function() {
			$('#topHtml').removeClass("loading");
			$('#ajaxLoader').css("display","none");
		});
	}
	//datetimepicker - date
	$('.input-group.date').each(function(index, value) {
		if(!$(value).datetimepicker()) {
			$(value).datetimepicker({
				viewMode: 'days',
				format: DT_FORMAT,
				allowInputToggle:true
			});
		}
	})
	
	//datetimepicker - month
	$('.input-group.month').each(function(index, value) {
		if(!$(value).datetimepicker()) {
			$(value).datetimepicker({
				viewMode: 'months',
				format: DT_YM_FORMAT,
				allowInputToggle:true
			});
		}
	})
});