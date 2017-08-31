import { Directive, Component,Input,ElementRef,Renderer2,OnInit } from '@angular/core';
import {NavController} from 'ionic-angular';

@Directive({
    selector: '[img-placeholder]',
     host : { 
         '[attr.src]': 'finalImage',
        
    }
  
})

export class PlaceholderDirective  implements OnInit{
    @Input('img-placeholder') targetSource: string;
  //  @Input('img-height') height;
    downloadingImage : any;
    finalImage: any;
    randomizePlaceholder;
    PlaceholderCss;
     baseUrl = "https://gola-gif-dev-store-cf.xpresso.me/R2luZ2Vy/";
    constructor(public element : ElementRef,
                public renderer: Renderer2){}

            

  defaultImageHeight:any;
  abc:any;
  // Set an input so the directive can set a default image.
  //@Input() defaultImage : string = 'assets/newloader.png';
  //@Input() defaultHeight : string = '100';
  
    
  ngOnInit() {
  //   this.defaultImageHeight = '100px';
    //console.log('defaultImageHeight',this.abc);

   // this.finalImage = this.defaultImage;
   //  this.PlaceholderCss = ['placeholder-1','placeholder-2','placeholder-3','placeholder-4','placeholder-5','placeholder-6'];
     // this.randomizePlaceholder = this.PlaceholderCss[Math.floor(Math.random() * this.PlaceholderCss.length)];
       this.renderer.addClass(this.element.nativeElement,'placeholder-2'); //add class dynamically  
    //edtited
    //this.defaultImageHeight = this.defaultHeight;


    console.log('Inside preloader');
    this.downloadingImage = new Image();  
    //this.downloadingImage.height = "110px";
    this.downloadingImage.onload = () => { //Once image is completed, console.log confirmation and switch our host attribute
      console.log('image downloaded');
      this.finalImage = this.baseUrl+this.targetSource;  //do the switch 
    }
   
    this.downloadingImage.src = this.baseUrl + this.targetSource;
    
  }
}