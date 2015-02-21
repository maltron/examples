//
//  Person.h
//  TestBackgroundSendData
//
//  Created by Mauricio Leal on 2/9/15.
//  Copyright (c) 2015 Mauricio "Maltron" Leal. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>


@interface Person : NSManagedObject

@property (nonatomic, retain) NSString * firstName;
@property (nonatomic, retain) NSString * lastName;
@property (nonatomic, retain) NSNumber * iD;

-(void)parseJSON:(NSData *)data;
-(void)parseJSONfromDictionary:(NSDictionary *)dict;
-(NSData *)json;
-(NSString *)toXML;

@end
