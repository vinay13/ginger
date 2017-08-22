import { Directive, Component,Input,ElementRef,Renderer,OnInit } from '@angular/core';
import {NavController} from 'ionic-angular';

@Directive({
    selector: '[img-preloader]',
    host : { 
         '[attr.src]': 'finalImage'
    }
})

export class PreLoaderDirective  implements OnInit{
 @Input('img-preloader') targetSource: string;
 
  downloadingImage : any; // In class holder of remote image
  finalImage: any; //property bound to our host attribute.
 
  // Set an input so the directive can set a default image.
  @Input() defaultImage : string = 'assets/preloader.gif';
 
  ngOnInit() {
    this.finalImage = this.defaultImage;
    console.log('Inside preloader');
 
    this.downloadingImage = new Image();  // create image object
    this.downloadingImage.onload = () => { //Once image is completed, console.log confirmation and switch our host attribute
      console.log('image downloaded');
      this.finalImage = this.targetSource;  //do the switch 😀
    }
    
    this.downloadingImage.src = this.targetSource;
  }

}