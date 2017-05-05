 import { Component } from '@angular/core';


 @Component({
     selector : 'page-idiom',
     templateUrl : 'idiom.html'
 })

 export class IdiomComponent{

     public selectedHero : string = "white";
     constructor(){
        //  this.selectedIdiom();
     }


     selectedIdiom(color){
         console.log('color',color);
        if (color === this.selectedHero) {
            return "white";
        } else {
            return "";
        }
     }
 }