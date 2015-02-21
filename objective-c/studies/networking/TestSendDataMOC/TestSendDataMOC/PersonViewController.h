//
//  PersonViewController.h
//  TestSendDataMOC
//
//  Created by Mauricio Leal on 2/18/15.
//  Copyright (c) 2015 Mauricio "Maltron" Leal. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ListPersistViewController.h"
#import "EntityPersistent.h"
#import "PersonCRUD.h"

@interface PersonViewController : ListPersistViewController<EntityPersistent, UITableViewDelegate>

-(id)initWithCRUD:(PersonCRUD *)crud;

@end
