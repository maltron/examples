//
//  PersonDetailViewController.h
//  TestMOC
//
//  Created by Mauricio Leal on 2/21/15.
//  Copyright (c) 2015 Mauricio "Maltron" Leal. All rights reserved.
//

#import "EntityDetailViewController.h"
#import "Person.h"
#import "PersonService.h"

@interface PersonDetailViewController : EntityDetailViewController<UITableViewDataSource>
@property (nonatomic, strong) Person *personSelected;

-(id)init:(PersonService *)service;
-(void)newPerson;
-(void)editPerson:(Person *)person;

@end
