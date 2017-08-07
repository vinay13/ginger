import { Directive, Component,Input,ElementRef,Renderer } from '@angular/core';
import {NavController} from 'ionic-angular';
@Directive({
    selector: '[hide-fab]',
    host : { 
        '(ionScroll)' : 'onContentScroll($event)'
    }
})


export class HideFabDirective {

    @Input('fab') fab: HTMLElement;
    headerHeight;
    scrollContent;
    constructor(public element : ElementRef,
                public renderer: Renderer,
                public navCtrl : NavController)
    {
        console.log('Hello directive');
    }  

    ngOnInit(){
        // this.header.parentElement.clientHeight;
      this.headerHeight = this.fab.clientHeight;
     // this.renderer.setElementStyle(this.fab,'webkitTransition','top 500ms');
       this.scrollContent = this.element.nativeElement.getElementsByClassName('scroll-content')[0];
         this.renderer.setElementStyle(this.scrollContent,'webkitTransition','display : none');
    }

    onContentScroll(event){
        console.log(event); 
             if(event.scrollTop > 56){
        //   this.renderer.setElementStyle(this.fab.nativeElement,'display','none');
          this.renderer.setElementStyle(this.scrollContent,'display','none');
        }
    }
}