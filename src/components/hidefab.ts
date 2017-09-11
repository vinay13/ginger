import { Directive, Component,Input,ElementRef,Renderer,ViewChild,EventEmitter, Output } from '@angular/core';
import {NavController} from 'ionic-angular';
@Directive({
    selector: '[hide-fab]',
    host : { 
        '(ionScroll)' : 'onContentScroll($event)'
    }
})


export class HideFabDirective {

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


    // @Output() onHide = new EventEmitter<boolean>();
    // setHide(){
    //    this.onHide.emit(true);
    // }


    ngOnInit(){
        
         this.scrollContent = this.element.nativeElement.getElementsByClassName('scroll-content')[0];
         //this.renderer.setElementStyle(this.scrollContent,'','display : none');
         
    }

    onContentScroll(event){
        console.log(event); 
             if(event.scrollTop > 56){
             //  this.renderer.setElementStyle(this.fab,'display','none');
             this.renderer.setElementStyle(this.element.nativeElement.firstChild,'visibility','hidden');
                this.recScroll = event.scrollTop;
                
             }

            if(event.directionY === 'up' ){
             this.renderer.setElementStyle(this.element.nativeElement.firstChild,'visibility','visible');
            }
    }
}