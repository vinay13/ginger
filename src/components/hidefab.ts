import { Directive, Component,Input,ElementRef,Renderer,ViewChild } from '@angular/core';
import {NavController} from 'ionic-angular';
@Directive({
    selector: '[hide-fab]',
    host : { 
        '(ionScroll)' : 'onContentScroll($event)'
    }
})


export class HideFabDirective {

    //  @ViewChild('fab') fabContent: any;

    @Input('fab') fab: any;
    headerHeight;
    scrollContent;
    recScroll : any;
    constructor(public element : ElementRef,
                public renderer: Renderer,
                public navCtrl : NavController)
    {
        console.log('Hello directive');
    }  

    ngOnInit(){
         // this.header.parentElement.clientHeight;
        // this.headerHeight = this.fab.clientHeight;
       //  this.renderer.setElementStyle(this.fab,'webkitTransition','top 500ms');
         this.scrollContent = this.element.nativeElement.getElementsByClassName('scroll-content')[0];
       //  this.renderer.setElementStyle(this.scrollContent,'','display : none');
    }

    onContentScroll(event){
        console.log(event); 
             if(event.scrollTop > 56){
             //  this.renderer.setElementStyle(this.fab,'display','none');
             this.renderer.setElementStyle(this.element.nativeElement.firstChild,'visibility','hidden');
                this.recScroll = event.scrollTop;
             }

            if(event.directionY === 'up' || event.deltaY < -56 ){
             this.renderer.setElementStyle(this.element.nativeElement.firstChild,'visibility','visible');
            }

            // if(!event.scrollTop){
            //  this.renderer.setElementStyle(this.element.nativeElement.firstChild,'visibility','visible');
            // }
    }
}