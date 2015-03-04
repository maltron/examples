//
//  Person.h
//  TestEnumeration
//
//  Created by Mauricio Leal on 2/27/15.
//  Copyright (c) 2015 Mauricio "Maltron" Leal. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>

typedef NS_ENUM(uint16_t, Status) {
    StatusLocalInserted,
    StatusLocalUpdated,
    StatusLocalDeleted,
    StatusRemoteInserted,
    StatusRemoteUpdated,
    StatusRemoteDeleted
};


@interface Person : NSManagedObject

@property (nonatomic, retain) NSNumber * iD;
@property (nonatomic, retain) NSString * firstName;
@property (nonatomic, retain) NSString * lastName;
@property (nonatomic) Status status;

@end
