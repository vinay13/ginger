import { Directive, Component,Input,ElementRef,Renderer,ApplicationRef,Injector } from '@angular/core';
import {NavController} from 'ionic-angular';

@Directive({
    selector: '[nav-transition]',
    host : { 
        '(ionScroll)' : 'onContentScroll($event)'
    }
})

export class NavbarTransitionDirective {

    //  appElementRef: ElementRef;
    @Input('navheader') header: HTMLElement;
    headerHeight;
    scrollContent;
    constructor(public element : ElementRef,
                public renderer: Renderer,
                public navCtrl : NavController,
                public appRef : ApplicationRef,
                public injector : Injector)
    {
        console.log('Hello directive');
    }  
    
     ngOnInit(){
        
      this.headerHeight = this.header.clientHeight;
       this.renderer.setElementStyle(this.header,'webkitTransition','background 1500ms');
        this.scrollContent = this.element.nativeElement.getElementsByClassName('scroll-content')[0];
        this.renderer.setElementStyle(this.scrollContent,'webkitTransition','margin-top : 700ms');
    }

    onContentScroll(event){
        console.log(event); 
        if(event.scrollTop > 56){ 
           
       //  this.renderer.setElementStyle(this.header,'top','-56px');
        //  this.renderer.setElementStyle(this.scrollContent,'top','-156px');
         this.renderer.setElementStyle(this.header,'background','#202E3F');
        //  this.renderer.setElementStyle(this.scrollContent,'margin-top','0px');
    } else{
         this.renderer.setElementStyle(this.header,'background','transparent');
        //  this.renderer.setElementStyle(this.scrollContent,'top','0px')
            // this.renderer.setElementStyle(this.header,'top','0px');
            // this.renderer.setElementStyle(this.scrollContent,'margin-top','56px')
        }
    }

 
}


