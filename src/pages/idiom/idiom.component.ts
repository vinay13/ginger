import { Component } from '@angular/core';
import { NavController,NavParams } from 'ionic-angular'; 
import { HomeComponent } from '../home/home.component'; 

 @Component({
     selector : 'page-idiom',
     templateUrl : 'idiom.html'
 })

 export class IdiomComponent{

     public selectedHero : string = "white";
     constructor( private navCtrl : NavController ,
                  private navparams : NavParams){
        //  this.selectedIdiom();
     }

     public idioms = [
                        {"id":1 , "name": "hindi"},
                        {"id":2 , "name": "english"},
                        {"id":3 , "name": "tamil"},
                        {"id":4 , "name": "telugu"},
                        {"id":5 , "name": "kannada"},
                        {"id":6 , "name": "malayalam"}
                    ];

     public selectedIndex: number = -1;
     public bgcolor : string ;
     public showbutton = true;

     selectedIdiom(index){
          this.selectedIndex = index;
          this.showbutton = false;
          console.log('index',index);    
     }

     navHomePage(){
         console.log('idiom',this.idioms[this.selectedIndex].name);
         this.navCtrl.push(HomeComponent,{
             'idiom': this.idioms[this.selectedIndex].name 
         });
     }

 }