import { Directive, Component,Input,ElementRef,Renderer,OnInit } from '@angular/core';
import {NavController} from 'ionic-angular';

@Directive({
    selector: '[img-preloader]',
    host : { 
         '[attr.src]': 'finalImage',
         '[attr.height]':'defaultImageHeight'
    }
})

export class PreLoaderDirective  implements OnInit{
 @Input('img-preloader') targetSource: string;
 @Input('img-height') targetHeight : string;
  downloadingImage : any; // In class holder of remote image
  finalImage: any; //property bound to our host attribute.
  defaultImageHeight:any;
  abc:any;
  // Set an input so the directive can set a default image.
  @Input() defaultImage : string = 'assets/preloader.gif';
  @Input() defaultHeight : string = '100';

  ngOnInit() {
  //   this.defaultImageHeight = '100px';
    //console.log('defaultImageHeight',this.abc);

    this.finalImage = this.defaultImage;
    
    //edtited
    this.defaultImageHeight = this.defaultHeight;


    console.log('Inside preloader');
    this.downloadingImage = new Image();  
    //this.downloadingImage.height = "110px";
    this.downloadingImage.onload = () => { //Once image is completed, console.log confirmation and switch our host attribute
      console.log('image downloaded');
      this.finalImage = this.targetSource;  //do the switch 
    }
   
    this.downloadingImage.src = this.targetSource;
    
  }

}