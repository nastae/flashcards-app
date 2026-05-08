import { Component } from '@angular/core';
import { MatAnchor } from "@angular/material/button";
import { GroupsRoutingModule } from "../../groups-routing-module";
import { MatIconModule } from "@angular/material/icon";

@Component({
  selector: 'app-mobile-nav',
  imports: [MatAnchor, GroupsRoutingModule, MatIconModule],
  templateUrl: './mobile-nav.html',
  styleUrl: './mobile-nav.scss',
})
export class MobileNav {}
