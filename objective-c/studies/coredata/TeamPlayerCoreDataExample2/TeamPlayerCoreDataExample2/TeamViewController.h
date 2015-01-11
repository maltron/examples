//
//  TeamViewController.h
//  TeamPlayerCoreDataExample2
//
//  Created by Mauricio Leal on 1/8/15.
//  Copyright (c) 2015 Mauricio "Maltron" Leal. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ListPersistViewController.h"
#import "EntityPersistent.h"

@interface TeamViewController : ListPersistViewController<EntityPersistent, UITableViewDelegate>

@end
