import { Directive, Component,Input,ElementRef,Renderer2,OnInit } from '@angular/core';
import {NavController} from 'ionic-angular';

@Directive({
    selector: '[img-placeholder]',
   
  
})

export class PlaceholderDirective  implements OnInit{
    @Input('img-placeholder') targetSource: string;
  //  @Input('img-height') height;
    downloadingImage : any;
    finalImage: any;
    randomizePlaceholder;
    PlaceholderCss;
    constructor(public element : ElementRef,
                public renderer: Renderer2){}

              
    ngOnInit(){
        // this.finalImage = '';
      this.PlaceholderCss = ['placeholder-1','placeholder-2','placeholder-3','placeholder-4','placeholder-5','placeholder-6'];
      this.randomizePlaceholder = this.PlaceholderCss[Math.floor(Math.random() * this.PlaceholderCss.length)];
       this.renderer.addClass(this.element.nativeElement,this.randomizePlaceholder); //add class dynamically  
    //    this.renderer.setStyle(this.element.nativeElement,'height','100px');
    // this.downloadingImage = new Image();  
  
    // this.downloadingImage.onload = () => { 
    //   this.finalImage = this.targetSource; 
    // }
    // this.downloadingImage.src = this.targetSource;
       
    }
}