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

     public idioms = [
                        {"id":1 , "name": "Hindi"},
                        {"id":2 , "name": "English"},
                        {"id":3 , "name": "Tamil"},
                        {"id":4 , "name": "Telugu"}
                    ];

     public selectedIndex: number = -1;
     public bgcolor : string ;
     selectedIdiom(index){
          this.selectedIndex = index;
          console.log('index',index);
        //  if( i == 0){
        //     this.bgcolor = "red";
        //  }
            
     }


 }