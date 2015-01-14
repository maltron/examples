//
//  Person.h
//  SampleJSON
//
//  Created by Mauricio Leal on 1/13/15.
//  Copyright (c) 2015 Mauricio "Maltron" Leal. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>


@interface Person : NSManagedObject

@property (nonatomic, retain) NSString * firstName;
@property (nonatomic, retain) NSString * lastName;

-(void)parseJSON:(NSData *)data;

@end
