import { Directive, Component,Input,ElementRef,Renderer } from '@angular/core';
import {NavController} from 'ionic-angular';
@Directive({
    selector: '[hide-header]',
    host : { 
        '(ionScroll)' : 'onContentScroll($event)'
    }
})

export class HideHeaderDirective {

    @Input('header') header: HTMLElement;
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
      this.headerHeight = this.header.clientHeight;
      this.renderer.setElementStyle(this.header,'webkitTransition','top 500ms');
       this.scrollContent = this.element.nativeElement.getElementsByClassName('scroll-content')[0];
        this.renderer.setElementStyle(this.scrollContent,'webkitTransition','margin-top : 700ms');
    }

    onContentScroll(event){
        console.log(event); 
        if(event.scrollTop > 56){ 
           
         this.renderer.setElementStyle(this.header,'top','-56px');
         this.renderer.setElementStyle(this.scrollContent,'margin-top','0px');
        } else{
            this.renderer.setElementStyle(this.header,'top','0px');
            this.renderer.setElementStyle(this.scrollContent,'margin-top','56px')
        }
    }

 
}


