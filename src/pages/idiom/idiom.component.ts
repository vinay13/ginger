import { Component } from '@angular/core';
import { NavController,NavParams } from 'ionic-angular'; 
import { HomeComponent } from '../home/home.component'; 
import { AboutPage } from '../about/about.ts';
import { Page2Page} from '../page2/page2.ts';
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
                        {"id":1 , "name": "Hindi"},
                        {"id":2 , "name": "English"},
                        {"id":3 , "name": "Tamil"},
                        {"id":4 , "name": "Telugu"},
                        {"id":5 , "name": "Kannada"},
                        {"id":6 , "name": "Malayalam"}
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
         this.navCtrl.push(AboutPage,{
             'idiom': this.idioms[this.selectedIndex].name 
         });
     }

 }