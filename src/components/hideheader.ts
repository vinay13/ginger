import { Directive, Component,Input,ElementRef,Renderer,ApplicationRef,Injector } from '@angular/core';
import {NavController} from 'ionic-angular';

@Directive({
    selector: '[hide-header]',
    host : { 
        '(ionScroll)' : 'onContentScroll($event)'
    }
})

export class HideHeaderDirective {

    //  appElementRef: ElementRef;
    @Input('header') header: HTMLElement;
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
        
        // let pElement =   this.element.nativeElement.;
        // this.appElementRef = this.injector.get(this.appRef.componentTypes[0]);
        // console.log('pElement',this.appElementRef);
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


