import { Component } from '@angular/core';
import { Observable } from 'rxjs';
import { Device } from '../device';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { DeviceService } from '../device.service';
import { HttpClient } from '@angular/common/http';
import { AddDeviceRequest } from '../add-device-request';
import { User } from '../user';
import { UserService } from '../user.service';

@Component({
  selector: 'app-admin-page',
  templateUrl: './admin-page.component.html',
  styleUrls: ['./admin-page.component.css']
})
export class AdminPageComponent {
  addDeviceRequest: AddDeviceRequest = {
    device: new Device(),
    user: new User()
  }
  
  form!: FormGroup;
  loading = false;
  showAllUsersComponent: boolean = false;
  showChat: boolean = false;
  submitted = false;
  description: string;
  address: string;
  maxConsumption: number;
  userName: string;
  userEmail: string;
  userPassword: string;

  constructor(private formBuilder: FormBuilder,
    private router: Router,
    private deviceService: DeviceService,
    private userService: UserService,
    private http: HttpClient
  ) { }

  ngOnInit() {
  }

  onSubmit() {
     this.addDeviceRequest.device.userEmail = this.userEmail;
     this.addDeviceRequest.device.description = this.description;
     this.addDeviceRequest.device.address = this.address;
     this.addDeviceRequest.device.maximumConsumption = this.maxConsumption;

     this.addDeviceRequest.user.name = this.userName;
     this.addDeviceRequest.user.email = this.userEmail;
     this.addDeviceRequest.user.password = this.userPassword;

     this.deviceService.addDevice(this.addDeviceRequest.device).subscribe(
       (response) => {
         console.log('Device and user added successfully:', response);
         this.userService.addUser(this.addDeviceRequest.user).subscribe();
         alert('Device and user added successfully!');
       },
       (error) => {
         console.error('Error adding device and user:', error);
         alert('Error adding device and user. Please try again.');
       }
     );
     this.showAllUsersComponent = false;
   }

   showAllUsers(){
    this.showAllUsersComponent = true;
    this.showChat = false;
   }

   showChatBox(){
    this.showChat = true;
    this.showAllUsersComponent = false;
   }
}