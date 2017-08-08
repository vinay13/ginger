import { Component,Input,OnInit,ViewChild,Renderer} from '@angular/core';


@Component({
    selector: 'accordion-page',
    templateUrl : 'accordion.html'
})

export class AccordionComponent implements OnInit{

    @ViewChild('cc') cardContent : any;
    accordionExpanded = false;
    constructor(public renderer : Renderer){}

    ngOnInit():void{
        console.log('card-content',this.cardContent);
        this.renderer.setElementStyle(this.cardContent.nativeElement,'webkitTransition','max-height 500ms,padding 500ms');
    }


    toggleContent(){
        if(this.accordionExpanded){
            this.renderer.setElementStyle(this.cardContent.nativeElement,'max-height','0px');
            this.renderer.setElementStyle(this.cardContent.nativeElement,'padding','0px 16px');
        }
        else{
            this.renderer.setElementStyle(this.cardContent.nativeElement,'max-height','500px');
            this.renderer.setElementStyle(this.cardContent.nativeElement,'padding','13px 16px');
        }
        this.accordionExpanded = !this.accordionExpanded;
    }

    
}