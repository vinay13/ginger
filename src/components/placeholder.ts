import { Directive, Component,Input,ElementRef,Renderer2,OnInit } from '@angular/core';
import {NavController} from 'ionic-angular';

@Directive({
    selector: '[img-placeholder]',
   
  
})

export class PlaceholderDirective  implements OnInit{
    @Input('img-placeholder') targetSource: string;
    downloadingImage : any;
    finalImage: any;
    
    constructor(public element : ElementRef,
                public renderer: Renderer2){
    
             }

              
    ngOnInit(){
        // this.finalImage = '';
      this.renderer.addClass(this.element.nativeElement, 'placeholder-1'); //add class dynamically  
    // this.downloadingImage = new Image();  
  
    // this.downloadingImage.onload = () => { 
    //   this.finalImage = this.targetSource; 
    // }
    // this.downloadingImage.src = this.targetSource;
       
    }
}